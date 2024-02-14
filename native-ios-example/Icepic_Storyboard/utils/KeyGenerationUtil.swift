//
//  KeyGenerationUtil.swift
//  IcePic_Storyboard
//
//  Created by Asanka Liysanage on 2023-11-21.
//
import UIKit
import SwiftJWT
import CommonCrypto
import SwiftyRSA

class Utils {
    
    static func getDeviceInfo() -> String {
        let device = UIDevice.current
        let systemVersion = device.systemVersion
        let nodename = ProcessInfo.processInfo.hostName

        return systemVersion + nodename
    }
    
    static func getSystemInfo() {
        // Get the iOS system version
        let systemVersion = UIDevice.current.systemVersion
        print("iOS System Version: \(systemVersion)")

        // Get the device name using uname
        var systemInfo = utsname()
        uname(&systemInfo)
        let deviceName = withUnsafePointer(to: &systemInfo.machine) {
            $0.withMemoryRebound(to: CChar.self, capacity: 1) {
                ptr in String(cString: ptr)
            }
        }
        print("Device Name: \(deviceName)")
    }
    
    static func createJwt(secret: String) -> String? {
        
        struct MyClaims: Claims {
            let iat: Date
            let exp: Date
        }

        do {
            
            let myHeader = Header()
            let iat = Date()
            let exp = Date(timeIntervalSinceNow: 3600)
            let myClaims = MyClaims(iat: iat, exp: exp)
            
            var myJWT = JWT(header: myHeader, claims: myClaims)
            let jwtSigner = JWTSigner.hs256(key: secret.data(using: .utf8)!)
            let signedJWT = try myJWT.sign(using: jwtSigner)

            return signedJWT
        } catch {
            return nil
        }
    }
    
    static func generatePasscode(passcode: String) -> String {
        do {
            guard let data = passcode.data(using: .utf8) else {
                return ""
            }

            var hashBytes = [UInt8](repeating: 0, count: Int(CC_SHA256_DIGEST_LENGTH))

            data.withUnsafeBytes { buffer in
                _ = CC_SHA256(buffer.baseAddress, CC_LONG(buffer.count), &hashBytes)
            }

            let hashData = Data(hashBytes)
            let hashString = hashData.map { String(format: "%02hhx", $0) }.joined()

            return hashString
        } catch {
            print("Error generating fingerprint: \(error.localizedDescription)")
            return ""
        }
    }
    
    static func generateFingerPrint(fingerprint: String) -> String {
        do {
            guard let data = (fingerprint + getDeviceInfo()).data(using: .utf8) else {
                return ""
            }

            var hashBytes = [UInt8](repeating: 0, count: Int(CC_SHA256_DIGEST_LENGTH))

            data.withUnsafeBytes { buffer in
                _ = CC_SHA256(buffer.baseAddress, CC_LONG(buffer.count), &hashBytes)
            }

            let hashData = Data(hashBytes)
            let hashString = hashData.map { String(format: "%02hhx", $0) }.joined()

            return hashString
        } catch {
            print("Error generating fingerprint: \(error.localizedDescription)")
            return ""
        }
    }

    static func generatePasscode(pwd: String) -> String? {
            guard let data = pwd.data(using: .utf8) else {
                return nil
            }

            var hashBytes = [UInt8](repeating: 0, count: Int(CC_SHA256_DIGEST_LENGTH))
            
            data.withUnsafeBytes { (buffer: UnsafeRawBufferPointer) in
                _ = CC_SHA256(buffer.baseAddress, CC_LONG(buffer.count), &hashBytes)
            }

            let hashData = Data(hashBytes)
            let hashString = hashData.map { String(format: "%02hhx", $0) }.joined()

            return hashString
        }
    
    static func decryptString(message: String) -> String? {
        do {
            let privateKey = try PrivateKey(base64Encoded: Constants.privateKey)
            let encrypted = try EncryptedMessage(base64Encoded: message)
            let clear = try encrypted.decrypted(with: privateKey, padding: .PKCS1)

            let data = clear.data
            let base64String = clear.base64String
            let string = try clear.string(encoding: .utf8)
            return string
        } catch let error {
            print("Error \(error.localizedDescription)")
            return nil
        }
    }
    
    static func encryptString(message: String) {
        do {
            let publicKey = try PublicKey(base64Encoded: Constants.publicKey)

            let clear = try ClearMessage(string: message, using: .utf8)
            let encrypted = try clear.encrypted(with: publicKey, padding: .PKCS1)

            let data = encrypted.data
            print(data)
            
            let base64String = encrypted.base64String
            print(base64String)

        } catch {
            print(error)
        }
    }
}
