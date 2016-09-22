package xie.com.androidcommon.util;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;

/**
 * Created by xiechengfa on 2016/9/22.
 * 工具类
 */
public class Utils {
    /**
     * 获取签名信息
     *
     * @param activity
     */
    public static void getSingInfo(Activity activity) {
        try {
            PackageInfo packageInfo = activity.getPackageManager()
                    .getPackageInfo("com.app.hero.ui",
                            PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            parseSignature(sign.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseSignature(byte[] signature) {
        try {
            CertificateFactory certFactory = CertificateFactory
                    .getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory
                    .generateCertificate(new ByteArrayInputStream(signature));
            String pubKey = cert.getPublicKey().toString();
            String signNumber = cert.getSerialNumber().toString();

            System.out.println("pubKey:" + pubKey);
            System.out.println("signNumber:" + signNumber);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }
}
