//
//  ThirdViewController.swift
//  IcePic_Storyboard
//
//  Created by Asanka Liyanage on 2023-11-20.
//

import UIKit
import WebKit

class ThirdViewController: UIViewController, WKScriptMessageHandler, WKNavigationDelegate {
    
    var pairCardLessResponse: [String: Any] = [:]
    var parsedMsgUserId: String?
    var parsedMsgIcePicToken: String?
    
    var fingerprint: String?
    
    @IBOutlet weak var pairCardResLbl: UILabel!
    @IBOutlet weak var circularProgress: UIActivityIndicatorView!
    @IBOutlet weak var continueRegBt: UIButton!

    @IBOutlet var picassoHashWV: WKWebView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let contentController = WKUserContentController()
        contentController.add(self, name: "picassoFPWebView")
        picassoHashWV.configuration.userContentController = contentController
        picassoHashWV.frame = view.bounds
        picassoHashWV!.navigationDelegate = self
        view.addSubview(picassoHashWV!)
        
//        picassoHashWV.isHidden = true
        circularProgress.isHidden = true
        
        loadBundlHtml()
    }
    
    func loadBundlHtml() {
        guard let fileUrl = Bundle.main.url(forResource: "picasso_fp", withExtension: "html") else { return }
        picassoHashWV!.loadFileURL(fileUrl, allowingReadAccessTo: fileUrl)
    }
    
    // Button action
    @IBAction func registerAndContinue(_ sender: Any) {
        // UI maintaninance
        self.pairCardResLbl.isHidden = true
        self.circularProgress.isHidden = false
        circularProgress.startAnimating()
        
        // Response data passed from previous controller
        // Decrypt from your private key and add the deviceId received from pair-cardless response
        let decryptedDeviceId: String = Utils.decryptString(message: pairCardLessResponse["deviceId"] as! String)!
        let authSecret: String = Utils.decryptString(message: pairCardLessResponse["authSecret"] as! String)!
        let deviceNo: String = Utils.decryptString(message: pairCardLessResponse["deviceNo"] as! String)!
        
        let jsonObject = [
            "deviceId": decryptedDeviceId,
            "authSecret": authSecret,
            "deviceNo": deviceNo
        ]
        
        do {
            let jsonData = try JSONSerialization.data(withJSONObject: jsonObject, options: .prettyPrinted)

            if let jsonString = String(data: jsonData, encoding: .utf8) {
                
                pairCardResLbl.text = jsonString
                
                pairCardResLbl.isHidden = false
            }
        } catch {
            print("Error converting JSON object to string: \(error.localizedDescription)")
        }
        
        registerDevice(decryptedDeviceId, parsedMsgUserId!, parsedMsgIcePicToken!, fingerprint ?? "")
    }
    
    func registerDevice(_ decryptedDeviceId: String, _ userId: String, _ icePicToken: String, _ fingerprint: String) {
        do {
            
            // Use the integration document given API endpoint
            let apiUrlString: String = "\(Constants.baseUrl)/user/registerDevice"
            
            guard let apiUrl = URL(string: apiUrlString) else {
                return
            }
            
            // Prepare the API request with headers
            // Use the parsedMessage contained icePicToken
            var request = URLRequest(url: apiUrl)
            request.httpMethod = "POST"
            request.setValue("application/json; charset=utf-8", forHTTPHeaderField: "Content-Type")
            // Include generated JWT from Sand dollar onboarding Auth Secret as the "Authorization"
            request.setValue("Bearer \(icePicToken)", forHTTPHeaderField: "Authorization")
            // Decrypted deviceId
            request.setValue(decryptedDeviceId, forHTTPHeaderField: "deviceId")
            
            // Decrypted device id
            // Use the your app's user fingerprint
            // Use the your app's user passcode
            // Use the integration document contained status
            // Use the integration document contained type
            // Use the parsedMessage contained userId
            let parameters: [String: Any] = [
                "deviceId": decryptedDeviceId,
                "fingerPrint": Utils.generateFingerPrint(fingerprint: ),
                "passcode": Utils.generatePasscode(passcode: "123456"),
                "status": "ACTIVE",
                "type": "WHITE_LABEL",
                "userId": userId
            ]
            
            do {
                // convert parameters to Data and assign dictionary to httpBody of request
                let jsonData = try? JSONSerialization.data(withJSONObject: parameters)
                request.httpBody = jsonData
              }
            
            // Include Sand dollar onboarding API key as the "apikeyid"
            let task = URLSession.shared.dataTask(with: request) { (data, response, error) in
                if let error = error {
                    print("Error: \(error)")
                    return
                }
                
                guard let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 else {
                    print("Error: \(String(describing: (response as? HTTPURLResponse)?.statusCode))")
                    return
                }
                if let data = data {
                    do {
                        let jsonResponse = try JSONSerialization.jsonObject(with: data, options: [])
                        let dict = jsonResponse as! [String: Any]
                        
                        // From here onward your app navigation / functionality can performed with the received data
                        // As example: navigating to a view controller
                        DispatchQueue.main.async {
                            
                            // Displaying response data
                            self.pairCardResLbl.isHidden = false
                            self.pairCardResLbl.text = (dict["token"] as! String)
                            self.circularProgress.stopAnimating()
                            self.circularProgress.isHidden = true
                            
                            // In this demo we are navigating to a another view controller to show the response and complete the flow
                            let tc = self.storyboard?.instantiateViewController(withIdentifier: "CompleteViewController") as! CompleteViewController
                            
                            tc.code = ["code": httpResponse.statusCode]
                            tc.message = ["message": dict]
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
    
    // Get the generated hash from picasso fingerprint webview canvas to swift code
    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        
        print(message.name)
        print(message.body)
        
        if message.name == "picassoFPWebView", let value = message.body as? String {
            fingerprint = value
        }
    }
}
