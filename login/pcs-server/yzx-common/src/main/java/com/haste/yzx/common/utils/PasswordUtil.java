package com.haste.yzx.common.utils;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class PasswordUtil {

    public static void main(String[] args) throws Exception {
        System.out.println("解密：" + decode("",""));
    }

    /**
    * Generate public and private keys
    */
    public static void generateKeyPair() {
        String publicKeyString = null;
        String privateKeyString = null;
        try {
            // Generate a key pair
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048); // Specifying the key length
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // Get the public and private keys
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // Convert the public and private keys into strings
            publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        // Print public and private key strings
        System.out.println("public: " + publicKeyString);
        System.out.println("private: " + privateKeyString);
    }

    /**
    * RSA Decryption
    */
    public static String decode(String encodePassword, String privateKey){
        try {
            // Decode the Base64 encoded private key
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);

            // Constructing a PKCS8EncodedKeySpec object
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

            // Get the key factory for the RSA algorithm
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            // Generate a private key object
            PrivateKey privateKeyObj = keyFactory.generatePrivate(keySpec);

            // Creating a decryptor
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKeyObj);

            // Decryption
            byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encodePassword));

            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
