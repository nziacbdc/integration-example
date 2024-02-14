//
//  CompleteViewController.swift
//  IcePic_Storyboard
//
//  Created by Asanka Liyanage on 2023-11-22.
//

import UIKit
import WebKit

class CompleteViewController: UIViewController {
    var code: Any?
    var message: Any?
    
    @IBOutlet weak var completeBt: UIButton!
    @IBOutlet weak var regDeviceResLbl: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let jsonObject = [
            "code": code,
            "message": message
        ]
        
        do {
            let jsonData = try JSONSerialization.data(withJSONObject: jsonObject, options: .prettyPrinted)

            if let jsonString = String(data: jsonData, encoding: .utf8) {
                regDeviceResLbl.text = jsonString
            }
        } catch {
            print("Error converting JSON object to string: \(error.localizedDescription)")
        }
    }
    
    func showProcessCompletedDialog() {
        let alertController = UIAlertController(title: "Process Completed",
                                                message: "Can use the available data in your app now",
                                                preferredStyle: .alert)

        let okAction = UIAlertAction(title: "OK", style: .default) { _ in
            
            if let navigationController = self.navigationController {
                navigationController.popToRootViewController(animated: true)
            } else {
                print("Navigation controller not found.")
            }
            let mainViewController = ViewController()
            self.present(mainViewController, animated: true, completion: nil)
        }

        alertController.addAction(okAction)

        present(alertController, animated: true, completion: nil)
    }
    
    @IBAction func completeIcePicProcess(_ sender: Any) {
        showProcessCompletedDialog()
    }
    
}
