package xie.com.androidcommon.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import xie.com.androidcommon.MyApplication;

/**
 * 设备信息
 */
public class DeviceInfoUtil {
//    DeviceInfoUtil() {
//        mModel = android.os.Build.MODEL;
//        mProduct = android.os.Build.PRODUCT;
//        mDevice = android.os.Build.DEVICE;
//        mBoard = android.os.Build.BOARD;
//        mBrand = android.os.Build.BRAND;
//        mSDKVer = String.valueOf(android.os.Build.VERSION.SDK_INT);
//        mManufacturer = android.os.Build.MANUFACTURER;
//        mFingerPrint = android.os.Build.FINGERPRINT;
//    }

    /**
     * 屏幕宽(px)
     *
     * @param activity
     * @return
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.widthPixels;
    }

    /**
     * 屏幕高(px)
     *
     * @param activity
     * @return
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.heightPixels;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBar() {
        // 方法1：
        // Class<?> c = null;
        // Object obj = null;
        // Field field = null;
        // int x = 0, sbar = 0;
        // try {
        // c = Class.forName("com.android.internal.R$dimen");
        // obj = c.newInstance();
        // field = c.getField("status_bar_height");
        // x = Integer.parseInt(field.get(obj).toString());
        // sbar = activity.getResources().getDimensionPixelSize(x);
        // } catch (Exception e1) {
        // e1.printStackTrace();
        // }
        //
        // return sbar;

        // 方法3：过不推荐使用，因为这个方法依赖于WMS(窗口管理服务的回调)
        // Rect rectangle= new Rect();
        // Window window= getWindow();
        // window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        // int statusBarHeight= rectangle.top;


        // 方法2
        int result = 0;
        int resourceId = ResUtil.getRes().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = ResUtil.getResDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取手机固件版本号(整型)
     *
     * @return
     */
    public static int getPhoneSdkVersion() {
        return Build.VERSION.SDK_INT;
    }


    /**
     * 获取手机固件版本号(字符串)
     *
     * @return
     */
    public static String getPhoneSDKVersionChar() {
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
     * 版本号内容(字符串)
     *
     * @return
     */
    public static String getVersionChars() {
        PackageManager packageManager = MyApplication.getInstance().getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(MyApplication.getInstance().getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        if (packageInfo == null) {
            return "1.0";
        }
        String versionStr = packageInfo.versionName;

        if (versionStr == null) {
            versionStr = "1.0";
        }

        return versionStr;
    }

    /**
     * 版本号(整型)
     *
     * @return
     */
    public static int getVersionInt() {
        PackageManager packageManager = MyApplication.getInstance().getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(MyApplication.getInstance().getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        if (packageInfo == null) {
            return 1;
        }
        return packageInfo.versionCode;
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
}
