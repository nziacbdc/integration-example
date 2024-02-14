//
//  ViewController.swift
//  IcePic_Storyboard
//
//  Created by Asanka Liyanage on 2023-11-10.
//

import UIKit
import WebKit

class ViewController: UIViewController {
    
    @IBOutlet weak var icePicLabel: UILabel!

    @IBOutlet weak var progressIndicator: UIActivityIndicatorView!
    @IBOutlet weak var icePicWVButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        progressIndicator.isHidden = true
        
    }
    
    @IBAction func generateAuthenticationToken(_ sender: Any) {
        self.icePicLabel.isHidden = true
        progressIndicator.isHidden = false
        progressIndicator.startAnimating()

        let apiUrlString = "\(Constants.baseUrlIce)/authenticate/token"

        guard let apiUrl = URL(string: apiUrlString) else {
            return
        }

        var request = URLRequest(url: apiUrl)
        request.httpMethod = "GET"
        request.setValue("application/json; charset=utf-8", forHTTPHeaderField: "Content-Type")
        request.setValue(Constants.icePicApiKey, forHTTPHeaderField: "apikeyid")
        request.setValue("Bearer \(Utils.createJwt(secret: Constants.icePicSecret)!)", forHTTPHeaderField: "Authorization")
        
        let task = URLSession.shared.dataTask(with: request) { (data, response, error) in
            if let error = error {
                print("Error: \(error)")
                return
            }
            
            guard let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 else {
                return
            }
            if let data = data {
                do {
                    let jsonResponse = try JSONSerialization.jsonObject(with: data, options: [])
                    let dict = jsonResponse as! [String: Any]
                    DispatchQueue.main.async {
                        self.icePicLabel.isHidden = false
                        self.icePicLabel.text = (dict["token"] as! String)
                        self.progressIndicator.stopAnimating()
                        self.progressIndicator.isHidden = true
                        
                        let destinationVC = self.storyboard?.instantiateViewController(withIdentifier: "SecondViewController") as! SecondViewController

                        destinationVC.icePicToken = (dict["token"] as! String)

                        self.navigationController?.pushViewController(destinationVC, animated: true)
                        
                    }
                } catch {
                    print("Error: \(error)")
                }
            }
        }

        task.resume()
    }
    
}
