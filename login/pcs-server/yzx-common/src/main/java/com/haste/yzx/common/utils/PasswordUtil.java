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
    * 生成公钥私钥
    */
    public static void generateKeyPair() {
        String publicKeyString = null;
        String privateKeyString = null;
        try {
            // 生成密钥对
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048); // 指定密钥长度
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // 获取公钥和私钥
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // 将公钥和私钥转换为字符串形式
            publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        // 打印公钥和私钥字符串
        System.out.println("公钥：" + publicKeyString);
        System.out.println("私钥：" + privateKeyString);
    }

    /**
    * RSA解密
    */
    public static String decode(String encodePassword, String privateKey){
        try {
            // 解码Base64编码的私钥
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);

            // 构造PKCS8EncodedKeySpec对象
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

            // 获取RSA算法的密钥工厂
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            // 生成私钥对象
            PrivateKey privateKeyObj = keyFactory.generatePrivate(keySpec);

            // 创建解密器
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKeyObj);

            // 解密
            byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encodePassword));

            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
