package xie.com.androidcommon.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import xie.com.androidcommon.MyApplication;

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
        int res=-1;
        String imsi = getTelephonyManager().getSubscriberId();
        if(imsi != null) {
            if(imsi.startsWith("46000") || imsi.startsWith("46002")){
                res=1;
            }else if(imsi.startsWith("46001")){
                res=2;
            }else if(imsi.startsWith("46003")){
                res=3;
            }
        }

        return res;
    }

    /**
     * 判断是否是平板（官方用法,一般是7寸以上是平板）
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
}
