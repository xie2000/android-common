package xie.com.androidcommon.utils.security;

import android.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class AESUtil {

    // SecretKey 负责保存对称密钥
    private static SecretKey deskey;
    // Cipher负责完成加密或解密工作
    private static Cipher c;
    // 该字节数组负责保存加密的结果
    private static byte[] cipherByte;

    static {
        initEnv();
    }

    private static void initEnv() {
        byte[] aesKey = readSaveKey();

        try {
            deskey = loadSecretKey(aesKey);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 生成Cipher对象,指定其支持的DES算法
        try {
            c = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public static byte[] createAESKey(int keySize) {
        byte[] keybuf = null;
        if (keySize == 0) {
            keySize = 128;
        }

        try {
            KeyGenerator t_keygen = KeyGenerator.getInstance("AES");
            t_keygen.init(keySize);
            //生成密钥
            SecretKey t_deskey = t_keygen.generateKey();
            keybuf = t_deskey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return keybuf;
    }

    public static String saveFixAESKey(int keySize) {
        byte[] tAESKeybuf = createAESKey(keySize);
        if (tAESKeybuf != null) {
            //混淆
            CommonSecurityToolUtil.mixByte(tAESKeybuf);
            String tString = Base64.encodeToString(tAESKeybuf, Base64.NO_WRAP);
            return tString;
        }
        return null;
    }

    /**
     * 根据设计的规则读取pubkey
     *
     * @return
     */
    private static byte[] readSaveKey() {
        String pukey = "39KLC1etSfUXuiGxRm5ilDDtqapKXm0COuHxUrWSWlc=";
        byte[] byteMixPubKey = Base64.decode(pukey, Base64.NO_WRAP);
        CommonSecurityToolUtil.mixByte(byteMixPubKey);
        return byteMixPubKey;


    }

    private static SecretKey loadSecretKey(byte[] keybuf) throws IOException {
        SecretKey t_desKey = new SecretKeySpec(keybuf, "AES");
        return t_desKey;

    }

    /**
     * 对字符串加密
     * @return
     */
    public static byte[] Encrytor(byte[] bytes) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // 根据密钥，对Cipher对象进行初始化，ENCRYPT_MODE表示加密模式
        if (c == null) {
            return null;
        }
        c.init(Cipher.ENCRYPT_MODE, deskey);
        // 加密，结果保存进cipherByte
        return c.doFinal(bytes);
    }

    /**
     * 对字符串解密
     *
     * @param buff
     * @return
     */
    public static byte[] Decryptor(byte[] buff) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // 根据密钥，对Cipher对象进行初始化，DECRYPT_MODE表示加密模式
        if (c == null) {
            return null;
        }
        c.init(Cipher.DECRYPT_MODE, deskey);
        return c.doFinal(buff);
    }

    public static String decryptText(byte[] buf) {
        try {
            byte[] strBuf = Decryptor(buf);
            String retStr = new String(strBuf, "UTF-8");

            return retStr;

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encryptText(String content) {
        byte[] byteContent = content.getBytes(Charset.forName("UTF-8"));
        try {
            byte[] ebytes = Encrytor(byteContent);
            return ebytes;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 对字符串加密
     * 先做对称加密，再做Base64转码
     */
    public static String encrypt(String str) throws Exception {
        c.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] src = str.getBytes(Charset.forName("UTF-8"));
        cipherByte = c.doFinal(src);
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(cipherByte);
    }

    /**
     * 对字符串解密
     * 先做Base64解码，再做对称解密
     */
    public static String decrypt(String str) throws Exception {
        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] buff = base64Decoder.decodeBuffer(str);
        c.init(Cipher.DECRYPT_MODE, deskey);
        cipherByte = c.doFinal(buff);
        return new String(cipherByte, "UTF-8");
    }

}
