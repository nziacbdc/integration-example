package com.movmintdigitalfiat.icepic.utils;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Cipher;

public class KeyPairGeneratorUtils {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String createJwt(String secret) {

        long iat = Instant.now().toEpochMilli();
        long exp = Instant.now().plusSeconds(30).toEpochMilli();

        try {
            return JWT.create()
                    .withIssuedAt(new Date(iat))
                    .withExpiresAt(new Date(exp))
                    .sign(Algorithm.HMAC256(secret));
        } catch (JWTCreationException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decrypt(String message) {
        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(Constants.privateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            byte[] messageBytes = Base64.getDecoder().decode(message);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] decryptedBytes = cipher.doFinal(messageBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generatePasscode(String pwd) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] pwdBytes = pwd.getBytes(StandardCharsets.UTF_8);
            byte[] hashBytes = sha256.digest(pwdBytes);
            return String.format("%064x", new java.math.BigInteger(1, hashBytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generateFingerPrint(String fingerprint, String deviceDetails) {

        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] pwdBytes = (fingerprint + deviceDetails).getBytes(StandardCharsets.UTF_8);
            byte[] hashBytes = sha256.digest(pwdBytes);
            return String.format("%064x", new java.math.BigInteger(1, hashBytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
