package xie.com.androidcommon.utils.security;

import android.util.Base64;

import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;


public class RSAUtil {

    public static RSAPublicKey cer_public = null;
    public static RSAPrivateKey cer_private = null;

    static {
        initEnv();
    }


    private static void initEnv() {
//

        byte[] byteMixPubKey = readPubKey();
        if (byteMixPubKey != null)
            try {
                cer_public = (RSAPublicKey) loadPublicKey(byteMixPubKey);
            } catch (Exception e) {
                e.printStackTrace();
            }

        byte[] bytePrivateKey = readPrivateKey();
        if (bytePrivateKey != null)
        {
            try {
                cer_private = (RSAPrivateKey) loadPrivateKey(bytePrivateKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 根据设计的规则读取pubkey
     *
     * @return
     */
    public static byte[] readPubKey() {
        String pukey = "BrCmBTU+MRKweb/CNTk5OTMxOrS1OAi5vzO4tDjt/q8CTNUIsd6S7sVu5QCyZi+GCSi0HvFYJf8Q2dLiAsmfNRyXHoAIyDOc4eV5t7iifyTphhy+0SdCkaJRTcGFEtdgLercVoC1qtjhFVN8AhQ4UdHnWk+lbeZ3uzGsYIGYd7FPeKZEqloxBerHrIeJ1atgL6nCq+HJ6FL1io9egTo7OTYw";
        byte[] byteMixPubKey = Base64.decode(pukey, Base64.NO_WRAP);
        CommonSecurityToolUtil.mixByte(byteMixPubKey);
        return byteMixPubKey;


    }


    public static byte[] readPrivateKey(){
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBANXGlzR97D2J5qrW81/cNYpeF74/GY0ryWAdxybo69c68acNKqYntTDwC6TX1ECCgJpHHN+3JYvpH3qplGB09L0q71gb2+VjuI2S4Nckakk6LABp59Zjep1V3k+NAJVVuaBPiXlJn3GSYgk93PaVsrHtk1gZmPue2fHQasO7tmu5AgMBAAECgYAbcu9xVwXhdPaAUy9WzLC3PpOv4uxF+yxVQh2TOm8l6LSkrYUfZpPWX8wu82OWkOswZBcqBps0ls9q8aFSrVHLSEE6Oc663rMZ+ErNU4YDIEcc86Oe5GijnsERrbtKyBUXpwOytLG/Af/8OEYUNEpZ/N3eTDATzoWXsSHWr0rknQJBAPC2Y40h2KreC1ZCmQUyRTW0aNm2dttHGfiJoakDnU/KDhK6XX8Uy0oAtB0RUAFXxGENntYKjI3dyGrzzdnJkrsCQQDjWkCsBjxhTN/kzBHRW+TLuR0fbpiiCyxBOv1zJ4NvaYpirG+m/r+Z+MvbVVNVhGJsT1rVLKh8Ds3GKN+EubYbAkAGx7xqH0fxGCwNk/andNEKQDir2T3j007h9058akOmGbTnxiQYDkfjn71zDCfKweQgL7gHo2huHo8kpBVqSwk9AkADTzsFxgl2+SGBOR9BRu8rsAQmgvuh0DvSr0MlO/wD0St8iDoP2kF7wk1lYfaWlhjArt8Jn17Mf4KrUcj/K5zlAkB1huAJK1qFZbJHz54xamlPRmJnCxeDX6P8eGiteTqoAMD67PhOUrcbnNrIO/+EGxfmXN/S7bnHAktsH6KJm+Y4";
        byte[] bytePrivateKey = Base64.decode(privateKey, Base64.NO_WRAP);
        return  bytePrivateKey;
    }

    public static byte[] encryptText(String sEncryptText) {
        // 重要:
        // 如果加密文本不满128位,要使用128位byte[],不足补0,否则跨平台不能互通
        int key_len = cer_public.getModulus().bitLength() / 8;
        String[] strAry = CommonSecurityToolUtil.splitString(sEncryptText, key_len-11);
//        byte[] en_content = new byte[128];
//        System.arraycopy(sEncryptText.getBytes(Charset.forName("UTF-8")), 0, en_content, 0, sEncryptText.getBytes(Charset.forName("UTF-8")).length);
        byte[] ret = encrypt(cer_public, sEncryptText.getBytes(Charset.forName("utf-8")));

        return ret;
    }

    public static byte[] decryptText(byte[] decryptBytes) {
        byte[] ret = new byte[0];
        ret = decrypt(cer_private, decryptBytes);
        return ret;
    }


    public static KeyPair createRSAKey(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(keySize, new SecureRandom());

        KeyPair keyPair = keyPairGen.generateKeyPair();
        return keyPair;
    }

    public static String saveKeyHex(PublicKey key) {
        // save public key
        String tString = CommonSecurityToolUtil.toHexString(key.getEncoded());
        return tString;
    }

    public static String saveKeyHex(PrivateKey key) {
        String tString = CommonSecurityToolUtil.toHexString(key.getEncoded());
        return tString;
    }

    public static String saveKeyB64(PublicKey key) {
        String tString = Base64.encodeToString(key.getEncoded(), Base64.NO_WRAP);
        return tString;
    }

    public static String saveKeyB64(PrivateKey key) {


        String tString = Base64.encodeToString(key.getEncoded(), Base64.NO_WRAP);
        return tString;
    }

    private static PublicKey loadPublicKey(byte[] bytesKey) throws Exception {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");


            X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(bytesKey);

            PublicKey publicKey = keyFactory.generatePublic(bobPubKeySpec);

            return publicKey;

        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }

    }

    private static PrivateKey loadPrivateKey(byte[] bytesKey) throws Exception {
        PrivateKey privateKey;

        try {
            // privateKey
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");



            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytesKey);

            privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);

        } catch (NoSuchAlgorithmException e) {

            throw new Exception("无此算法");

        } catch (InvalidKeySpecException e) {

            throw new Exception("私钥非法");

        } catch (NullPointerException e) {

            throw new Exception("私钥数据为空");

        }
        return privateKey;
    }

    private static byte[] encrypt(RSAPublicKey publicKey, byte[] data) {
        if (publicKey != null) {
            try {
                Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                return cipher.doFinal(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static byte[] decrypt(RSAPrivateKey privateKey, byte[] raw) {
        if (privateKey != null) {
            try {
                Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                return cipher.doFinal(raw);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}