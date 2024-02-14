//
//  SecondViewController.swift
//  IcePic_Storyboard
//
//  Created by Asanka Liyanage on 2023-11-20.
//

import UIKit
import WebKit

class SecondViewController: UIViewController, WKNavigationDelegate, WKScriptMessageHandler {
    
    var icePicWebView: WKWebView!
    
    var icePicToken: String?
    
    
    @IBOutlet weak var progressIndicator: UIActivityIndicatorView!
    @IBOutlet weak var icePicWVButton: UIButton!
    @IBOutlet weak var icePicLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationItem.setHidesBackButton(true, animated: true)
        
        if let data = icePicToken {
            print("Received data: \(data)")
        }
        
        // Prepare WebView configurations to receive and send messages
        let configuration = WKWebViewConfiguration()
        
        // This UserContentController is required to listen and get the data from WebView to Swift
        let contentController = WKUserContentController()

        contentController.add(self, name: "inappwebview")

        configuration.userContentController = contentController
        
        // Attach prepared configs to webview
        icePicWebView = WKWebView(frame: view.bounds, configuration: configuration)
        icePicWebView!.navigationDelegate = self
        view.addSubview(icePicWebView!)
        
        // Add constraints
        NSLayoutConstraint.activate([
            icePicWebView!.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            icePicWebView!.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            icePicWebView!.topAnchor.constraint(equalTo: view.topAnchor),
            icePicWebView!.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])

        // Clearing webview cache to make sure previous data not causing any issues
        let dataStore = WKWebsiteDataStore.default()
        dataStore.fetchDataRecords(ofTypes: WKWebsiteDataStore.allWebsiteDataTypes()) { (records) in
            for record in records {
                dataStore.removeData(ofTypes: WKWebsiteDataStore.allWebsiteDataTypes(), for: [record], completionHandler: {
                    print("Deleted: " + record.displayName);
                })
            }
        }
        
        // Clearing webview cookies
        HTTPCookieStorage.shared.removeCookies(since: Date.distantPast)
        
        // Load the webpage within webview
        if let url = URL(string: "\(Constants.webViewUrl)/#/3rd-party/self-onboarding") {
            let request = URLRequest(url: url)
            icePicWebView!.load(request)
        }
    }
    
    func postMessageToWorker() {
        let script = "window.postMessage({\"icepicToken\": \"\(icePicToken ?? "")\", \"apikeyid\":\"\(Constants.icePicApiKey)\"});"
        icePicWebView!.evaluateJavaScript(script)
    }
    
    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        if message.name == "inappwebview", let value = message.body as? String {
            
            // You can use the any preferred method to decode this message to a JSON object or any type
            // To demonstrate we are using JSONSerialization to decode the message parameter to a Swift dictionary
            if let message = value.data(using: .utf8) {
                do {
                    if let parsedMessageData = try JSONSerialization.jsonObject(with: message, options: []) as? [String: Any] {
                        // Refer the provided IcePic integration documentation to see what Key-value pairs are included in the JSON body
                        // First one is `loaded` to verify if the web page is ready and expecting event from your mobile to the WebView
                        if (parsedMessageData["loaded"] != nil) {
                            // We just have one action which is to send your icepicToken and apikeyid provided by MovMint dev team to Web app using window.postMessage({})
                            let loaded = parsedMessageData["loaded"] as? Bool ?? false
                            // Use the IOS WebView available method `evaluateJavascript()` to run the JS code snippet
                            // Send the `icepicToken` using the Web API window.postMessage()
                            // Expected argument should be in this format: {"icepicToken": "<your token>", "apikeyid": "<your api key id>"}
                            if (loaded) {
                                postMessageToWorker()
                            }
                        } else if (parsedMessageData["isBacked"] != nil) {
                            self.navigationController?.popViewController(animated: true)
                        } else if (parsedMessageData["icepicToken"] != nil) {
                            // The next JSON property to check from the parsed/deserialized message is `data`
                            // Once `data` property becomes available with data, can initiate the pair-cardless API call with available data
                            pairCardLess(parsedMessageData);
                        }
                    }
                } catch {
                    print("Error parsing JSON: \(error)")
                }
            }
        }
    }
    
    // From the passing `data` argument few properties are required to do the API calls. They are,
    // "data", "userId", and "icepicToken"
    // Follow the integration documentation and do the API call with required info as you wish
    // Following is a sample API request using URLSession
    func pairCardLess(_ parsedMessageData: [String: Any?]) {
        do {
            
            // Endpoint
            let apiUrlString: String = "\(Constants.baseUrl)/devices/pair-cardless"
            guard let apiUrl = URL(string: apiUrlString) else {
                return
            }
            
            // Get the device name
            let deviceName = UIDevice.current.name
            
            // Prepare the Request body with received deserialized message "data"
            // Activation code is included in the deserialized/passed message in the format of "nzia:activate/<activation code>"
            // Replace the "nzia:activate/" from the data property / key and include in request body "activationCode" property
            var activationCode = parsedMessageData["activationCode"] as! String
            activationCode = activationCode.replacingOccurrences(of: "nzia:activate/", with: "")
            
            // Generate a deviceId
            let generatedUuid: String = UUID().uuidString
            
            // Prepare the request with headers and type
            var request = URLRequest(url: apiUrl)
            request.httpMethod = "POST"
            // Include API request required headers
            request.setValue("application/json; charset=utf-8", forHTTPHeaderField: "Content-Type")
            // Include Sand dollar onboarding API key as the "apikeyid"
            request.setValue(Constants.sandDollarApiKey, forHTTPHeaderField: "apikeyid")
            // Include generated JWT from Sand dollar onboarding Auth Secret as the "Authorization"
            request.setValue("Bearer \(Utils.createJwt(secret: Constants.sandDollarSecret)!)", forHTTPHeaderField: "Authorization")
            // Include generated device id
            request.setValue(generatedUuid, forHTTPHeaderField: "deviceId")
            
            // Prepare the request body params to send
            let parameters: [String: Any] = [
                "activationCode": activationCode,
                "publicKey": Constants.publicKey,
                "model": deviceName
            ]
            
            do {
                // Swift dictionary to Json data convertion
                let jsonData = try? JSONSerialization.data(withJSONObject: parameters)
                // convert parameters to Data and assign dictionary to httpBody of request
                request.httpBody = jsonData
              } catch let error {
                print(error.localizedDescription)
                return
            }
            
            // Execute the preapred http request
            let task = URLSession.shared.dataTask(with: request) { (data, response, error) in
                if let error = error {
                    print("Error: \(error)")
                    return
                }
                
                guard let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 else {
                    print("Error: \(String(describing: (response as? HTTPURLResponse)?.statusCode))")
                    return
                }
                
                // If the http request is successful with 200, we can pass the response body and userId, icePicToken and etc.
                if let data = data {
                    do {
                        let jsonResponse = try JSONSerialization.jsonObject(with: data, options: [])
                        let dict = jsonResponse as! [String: Any]
                        
                        // Here we are navigating to an another screen With the received response to keep it simple
                        // In your app, can do any UI or task based on your usecases
                        // But keep remember we have one more request to send to register device with some of the received response data
                        DispatchQueue.main.async {
                            self.icePicWebView?.isHidden = true
                            
                            let tc = self.storyboard?.instantiateViewController(withIdentifier: "ThirdViewController") as! ThirdViewController
                            tc.pairCardLessResponse = dict
                            tc.parsedMsgUserId = (parsedMessageData["userId"] as! String)
                            tc.parsedMsgIcePicToken = (parsedMessageData["icepicToken"] as! String)
                            self.navigationController?.pushViewController(tc, animated: true)
                        }
                    } catch {
                        print("Error: \(error)")
                    }
                }
            }
            task.resume()
        }
    }
}
