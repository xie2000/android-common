package xie.com.androidcommon.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import xie.com.androidcommon.MyApplication;

/**
 * 设备信息
 */
public enum DeviceInfoUtil {

    SINGLETON_INSTANCE;

    public String mManufacturer = "";
    public String mFingerPrint = "";
    public String mModel = "";
    public String mProduct = "";
    public String mDevice = "";
    public String mBoard = "";
    public String mBrand = "";
    public String mSDKVer = "";


    public static DeviceInfoUtil getInstance() {
        return SINGLETON_INSTANCE;
    }

    DeviceInfoUtil() {
        mModel = android.os.Build.MODEL;
        mProduct = android.os.Build.PRODUCT;
        mDevice = android.os.Build.DEVICE;
        mBoard = android.os.Build.BOARD;
        mBrand = android.os.Build.BRAND;
        mSDKVer = String.valueOf(android.os.Build.VERSION.SDK_INT);
        mManufacturer = android.os.Build.MANUFACTURER;
        mFingerPrint = android.os.Build.FINGERPRINT;
    }

    /**
     * 屏幕宽(px)
     * @param activity
     * @return
     */
    public static int getScreenWidth(Activity activity){
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.widthPixels;
    }

    /**
     * 屏幕高(px)
     * @param activity
     * @return
     */
    public static int getScreenHeight(Activity activity){
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.heightPixels;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBar(){
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
        int resourceId =  ResUtil.getRes().getIdentifier("status_bar_height", "dimen", "android");
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
     * @param context
     * @return
     */
    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * IMEI
     * 权限：android.permission.READ_PHONE_STATE
     * @return
     */
    public static String getIMEI() {
        TelephonyManager tm = (TelephonyManager) MyApplication.getInstance()
                .getSystemService(Context.TELEPHONY_SERVICE);

        if (tm == null) {
            return "000000000000000";
        }

        String imei = tm.getDeviceId();
        if (imei == null || imei.length() <= 0) {
            return "000000000000000";
        }

        return imei;
    }
}
