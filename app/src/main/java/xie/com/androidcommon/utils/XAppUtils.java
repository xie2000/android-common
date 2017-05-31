package xie.com.androidcommon.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.io.File;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.List;

import xie.com.androidcommon.MyApplication;

/**
 * 应用工具类.
 */
public class XAppUtils {
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
        int resourceId = ResUtils.getRes().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = ResUtils.getResDimensionPixelSize(resourceId);
        }
        return result;
    }

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
     * 读取application 节点  meta-data 信息
     */
    public static String readMetaDataFromApplication(String key) {
        try {
            ApplicationInfo appInfo = MyApplication.getInstance().getPackageManager()
                    .getApplicationInfo(MyApplication.getInstance().getPackageName(),
                            PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 描述: 打开App
     *
     * @param packageName 包名
     */
    public static void startApp(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return;
        }

        MyApplication.getInstance().startActivity(MyApplication.getInstance().getPackageManager().getLaunchIntentForPackage(packageName));
    }

    /**
     * 是否安装了指定包名的App
     *
     * @param packageName App包名
     * @return
     */
    public static boolean isInstallApp(String packageName) {
        PackageManager manager = MyApplication.getInstance().getPackageManager();
        List<PackageInfo> pkgList = manager.getInstalledPackages(0);
        for (int i = 0; i < pkgList.size(); i++) {
            PackageInfo info = pkgList.get(i);
            if (info.packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    /**
     * 描述：打开并安装文件.
     *
     * @param file apk文件路径
     */
    public static void installApk(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        MyApplication.getInstance().startActivity(intent);
    }

    /**
     * 描述：卸载程序.
     *
     * @param packageName 包名
     */
    public static void uninstallApk(String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        MyApplication.getInstance().startActivity(intent);
    }

    /**
     * need < uses-permission android:name ="android.permission.GET_TASKS"/>
     * <p>
     * 判断是否前台运行
     * <p>
     * 之前，使用该接口需要 android.permission.GET_TASKS
     * 即使是自己开发的普通应用，只要声明该权限，即可以使用getRunningTasks接口。
     * 但从L开始，这种方式以及废弃。
     * 应用要使用该接口必须声明权限android.permission.REAL_GET_TASKS
     * 而这个权限是不对三方应用开放的。（在Manifest里申请了也没有作用）
     * 系统应用（有系统签名）可以调用该权限。
     */
    public static boolean isRunningForeground() {
        ActivityManager am = (ActivityManager) MyApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName componentName = taskList.get(0).topActivity;
            if (componentName != null && componentName.getPackageName().equals(MyApplication.getInstance().getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 用来判断服务是否运行.
     *
     * @param className 判断的服务名字 "com.xxx.xx..XXXService"
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) MyApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> servicesList = activityManager.getRunningServices(Integer.MAX_VALUE);
        Iterator<ActivityManager.RunningServiceInfo> l = servicesList.iterator();
        while (l.hasNext()) {
            ActivityManager.RunningServiceInfo si = (ActivityManager.RunningServiceInfo) l.next();
            if (className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    /**
     * 停止服务.
     *
     * @param className the class name
     * @return true, if successful
     */
    public static boolean stopRunningService(String className) {
        Intent intent = null;
        boolean ret = false;
        try {
            intent = new Intent(MyApplication.getInstance(), Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intent != null) {
            ret = MyApplication.getInstance().stopService(intent);
        }
        return ret;
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
     * 获取应用签名
     *
     * @param pkgName 包名
     * @return 返回应用的签名
     */
    public static String getSign(String pkgName) {
        try {
            PackageInfo pis = MyApplication.getInstance().getPackageManager()
                    .getPackageInfo(pkgName,
                            PackageManager.GET_SIGNATURES);
            return hexDigest(pis.signatures[0].toByteArray());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 将签名字符串转换成需要的32位签名
     *
     * @param paramArrayOfByte 签名byte数组
     * @return 32位签名字符串
     */
    private static String hexDigest(byte[] paramArrayOfByte) {
        final char[] hexDigits = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97,
                98, 99, 100, 101, 102};
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            for (int i = 0, j = 0; ; i++, j++) {
                if (i >= 16) {
                    return new String(arrayOfChar);
                }
                int k = arrayOfByte[i];
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                arrayOfChar[++j] = hexDigits[(k & 0xF)];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0   支持4.1.2,4.1.23.4.1.rc111这种形式
     *
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) throws Exception {
        if (version1 == null || version2 == null) {
            throw new Exception("compareVersion xloading_error:illegal params.");
        }
        String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."；
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
        int diff = 0;
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
            ++idx;
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }
}
