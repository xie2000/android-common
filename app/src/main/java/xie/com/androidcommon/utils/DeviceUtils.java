package xie.com.androidcommon.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import xie.com.androidcommon.MyApplication;

import static android.content.ContentValues.TAG;

/**
 * 应用工具类.
 */
public class DeviceUtils {
    /**
     * 获取手机固件版本号(整型)
     *
     * @return
     */
    public static int getPhoneSdkVersionCode() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机固件版本号(字符串)
     *
     * @return
     */
    public static String getPhoneSDKVersionName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 手机型号
     *
     * @return
     */
    public static String getHandsetType() {
        return android.os.Build.MODEL;
    }

    /**
     * 平台
     *
     * @return
     */
    public static String getPlatForm() {
        return "mobile.anroid";// android
    }

    /**
     * 获取版本名称
     * String
     *
     * @return 当前应用的版本名称
     */
    public static String getAppVersionName(Context context) {
        return getPackageInfo().versionName;
    }

    /**
     * 获取版本号
     * int
     *
     * @return 当前应用的版本号
     */
    public static int getAppVersionCode(Context context) {
        return getPackageInfo().versionCode;
    }

    /**
     * 获取mac地址
     * 权限：android.permission.ACCESS_WIFI_STATE
     *
     * @param context
     * @return
     */
    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * 获取TelephonyManager
     *
     * @return
     */
    public static TelephonyManager getTelephonyManager() {
        return (TelephonyManager) MyApplication.getInstance()
                .getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * IMEI
     * 权限：android.permission.READ_PHONE_STATE
     *
     * @return
     */
    public static String getIMEI() {
        //如果Android Pad没有IMEI,用此方法获取设备ANDROID_ID：
        //String android_id = Settings.Secure.getString(MyApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
        return getTelephonyManager().getDeviceId();
    }

    /**
     * 获取运营商sim卡imsi号
     *
     * @return
     */
    public static String getIMSI() {
        return getTelephonyManager().getSubscriberId();
    }

    /**
     * 获取SIM卡类型
     *
     * @return -1:失败，1：移动，2：联通，3：电信
     */
    public static int isChinaMobileSIM() {
        int res = -1;
        String imsi = getTelephonyManager().getSubscriberId();
        if (imsi != null) {
            if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
                res = 1;
            } else if (imsi.startsWith("46001")) {
                res = 2;
            } else if (imsi.startsWith("46003")) {
                res = 3;
            }
        }

        return res;
    }

    /**
     * 判断是否是平板（官方用法,一般是7寸以上是平板）
     *
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 获取PackageInfo
     *
     * @return PackageInfo
     */
    public static PackageInfo getPackageInfo() {
        PackageManager packageManager = MyApplication.getInstance().getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(MyApplication.getInstance().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }

    /**
     * 生成一个设备指纹（耗时50毫秒以内）：
     * 1.IMEI + 设备硬件信息（主要）+ ANDROID_ID + WIFI MAC组合成的字符串
     * 2.用MessageDigest将以上字符串处理成32位的16进制字符串
     * @return 设备指纹
     */
    public static String createFingerprint() {
        long startTime = System.currentTimeMillis();

        // 1.IMEI
        final String imei = getIMEI();

        Log.i(TAG, "imei=" + imei);

        //2.android 设备信息（主要是硬件信息）
        final String hardwareInfo = Build.ID + Build.DISPLAY + Build.PRODUCT
                + Build.DEVICE + Build.BOARD /*+ Build.CPU_ABI*/
                + Build.MANUFACTURER + Build.BRAND + Build.MODEL
                + Build.BOOTLOADER + Build.HARDWARE /* + Build.SERIAL */
                + Build.TYPE + Build.TAGS + Build.FINGERPRINT + Build.HOST
                + Build.USER;
        //Build.SERIAL => 需要API 9以上
        Log.i(TAG, "hardward info=" + hardwareInfo);

        /* 3. Android_id 刷机和恢复出厂会变
         * A 64-bit number (as a hex string) that is randomly
        * generated when the user first sets up the device and should remain
        * constant for the lifetime of the user's device. The value may
        * change if a factory reset is performed on the device.
        */
        final String androidId = Settings.Secure.getString(MyApplication.getInstance().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.i(TAG, "android_id=" + androidId);


        /**
         * 4. The WLAN MAC Address string（个别手机刚开机完成后会获取不到，舍去）
         */
        /*WifiManager wifiMgr = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        final String wifiMAC = wifiMgr.getConnectionInfo().getMacAddress();
        Log.i(TAG,"wifi Mac="+wifiMAC);*/


        /*
         *  5. get the bluetooth MAC Address
         *  （有部分手机，如三星GT-S5660 2.3.3，当蓝牙关闭时，获取不到蓝牙MAC;
         *   所以为了保证 device id 的不变，舍去）
         */
        /*BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String bt_MAC = null;
        if (bluetoothAdapter == null) {
            Log.e(TAG, "bluetoothAdapter is null");
        } else {
            bt_MAC = bluetoothAdapter.getAddress();
        }
        Log.i(TAG,"m_szBTMAC="+bt_MAC);*/


        // Combined Device ID
        final String deviceId = imei + hardwareInfo + androidId/* + wifiMAC + bt_MAC*/;
        Log.i(TAG, "deviceId=" + deviceId);

        // 创建一个 messageDigest 实例
        MessageDigest msgDigest = null;
        try {
            msgDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //用 MessageDigest 将 deviceId 处理成32位的16进制字符串
        msgDigest.update(deviceId.getBytes(), 0, deviceId.length());
        // get md5 bytes
        byte md5ArrayData[] = msgDigest.digest();

        // create a hex string
        String deviceUniqueId = new String();
        for (int i = 0; i < md5ArrayData.length; i++) {
            int b = (0xFF & md5ArrayData[i]);
            // if it is a single digit, make sure it have 0 in front (proper
            // padding)
            if (b <= 0xF) deviceUniqueId += "0";
            // add number to string
            deviceUniqueId += Integer.toHexString(b);
//          Log.i(TAG,"deviceUniqueId=" + deviceUniqueId);
        }
        // hex string to uppercase
        deviceUniqueId = deviceUniqueId.toUpperCase();
        Log.d(TAG, "生成的设备指纹：" + deviceUniqueId);
        Log.e(TAG, "生成DeviceId 耗时：" + (System.currentTimeMillis() - startTime));

        return deviceUniqueId;
    }
}
