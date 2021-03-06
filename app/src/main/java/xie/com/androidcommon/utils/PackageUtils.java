package xie.com.androidcommon.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import xie.com.androidcommon.MyApplication;

/**
 * Created by xiechengfa on 2016/9/22.
 * 应用包工具类
 */
public class PackageUtils {
    public static final int APP_INSTALL_AUTO = 0;
    public static final int APP_INSTALL_INTERNAL = 1;
    public static final int APP_INSTALL_EXTERNAL = 2;

    /**
     * 检查指定包是否已安装
     *
     * @param context     系统变量
     * @param packageName 包名
     */
    public static boolean isInstalled(Context context, String packageName) {
        if (context == null || TextUtils.isEmpty(packageName)) {
            return false;
        }

        final List<PackageInfo> listPackageInfos = context.getPackageManager().getInstalledPackages(0);
        if (listPackageInfos != null && listPackageInfos.size() > 0) {
            for (PackageInfo packageInfo : listPackageInfos) {
                String pkgName = packageInfo.packageName;
                if (!TextUtils.isEmpty(pkgName) && pkgName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 安装APk
     */
    public static boolean installApk(String path) {
        File file = new File(path);
        if (file == null || !file.exists() || !file.isFile() || file.length() <= 0) {
            return false;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        //intent.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
        MyApplication.getInstance().startActivity(intent);

        return true;
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
     * 启动应用
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
     * 启动应用
     */
    public static boolean startAppByPackageName(Context context, String packageName, Map<String, String> param) {
        android.content.pm.PackageInfo pi = null;
        try {
            pi = context.getPackageManager().getPackageInfo(packageName, 0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
                resolveIntent.setPackage(pi.packageName);
            }

            List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(resolveIntent, 0);

            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                String packageName1 = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);

                ComponentName cn = new ComponentName(packageName1, className);

                intent.setComponent(cn);
                if (param != null) {
                    for (Map.Entry<String, String> en : param.entrySet()) {
                        intent.putExtra(en.getKey(), en.getValue());
                    }
                }
                context.startActivity(intent);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context.getApplicationContext(), "启动失败",
                    Toast.LENGTH_LONG).show();
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
     * 判断当前进程是否存在
     *
     * @param pkg     进程包名
     * @param context 上下文实例
     */
    public static boolean isProcessExist(String pkg, Context context) {
        boolean isExist = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 获取系统中所有正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        // 获取当前activity所在的进程
        // 对系统中所有正在运行的进程进行迭代，如果进程名不是当前进程，则Kill掉
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
            if (appProcessInfo.processName.equals(pkg)) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    /**
     * 获取App签名信息
     *
     * @param packageName:应用的包名
     */
    public static void getAppSingInfo(String packageName) {
        try {
            PackageInfo packageInfo = MyApplication.getInstance().getPackageManager()
                    .getPackageInfo(packageName,
                            PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            parseSignature(sign.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //解析签名
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

    /**
     * 启动APK的默认Activity
     *
     * @param ctx
     * @param packageName
     */
    public static void startApkActivity(final Context ctx, String packageName) {
        PackageManager pm = ctx.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(packageName, 0);
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setPackage(pi.packageName);

            List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);

            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                String className = ri.activityInfo.name;
                intent.setComponent(new ComponentName(packageName, className));
                ctx.startActivity(intent);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取应用程序下所有Activity
     *
     * @param ctx
     * @return
     */
    public static ArrayList<String> getActivities(Context ctx) {
        ArrayList<String> result = new ArrayList<String>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.setPackage(ctx.getPackageName());
        for (ResolveInfo info : ctx.getPackageManager().queryIntentActivities(intent, 0)) {
            result.add(info.activityInfo.name);
        }
        return result;
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

    /**
     * 是否为系统应用
     *
     * @param packageName
     * @return
     */
    public static boolean isSystemApplication(String packageName) {
        ApplicationInfo app = null;
        try {
            app = MyApplication.getInstance().getPackageManager().getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return (app != null && (app.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
    }

    /**
     * 调用系统分享
     */
    public static void shareToOtherApp(Context context, String title, String content, String dialogTitle) {
        Intent intentItem = new Intent(Intent.ACTION_SEND);
        intentItem.setType("text/plain");
        intentItem.putExtra(Intent.EXTRA_SUBJECT, title);
        intentItem.putExtra(Intent.EXTRA_TEXT, content);
        intentItem.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intentItem, dialogTitle));
    }

    /**
     * 打开已安装应用的详情
     */
    public static void goToInstalledAppDetails(Context context, String packageName) {
        Intent intent = new Intent();
        int sdkVersion = Build.VERSION.SDK_INT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", packageName, null));
        } else {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra((sdkVersion == Build.VERSION_CODES.FROYO ? "pkg"
                    : "com.android.settings.ApplicationPkgName"), packageName);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 获取当前系统安装应用的默认位置
     *
     * @return APP_INSTALL_AUTO or APP_INSTALL_INTERNAL or APP_INSTALL_EXTERNAL.
     */
    public static int getInstallLocation() {
        ShellUtil.CommandResult commandResult = ShellUtil.execCommand(
                "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm get-install-location", false, true);
        if (commandResult.result == 0 && commandResult.responseMsg != null && commandResult.responseMsg.length() > 0) {
            try {
                return Integer.parseInt(commandResult.responseMsg.substring(0, 1));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return APP_INSTALL_AUTO;
    }


//    /**
//     * 收起状态栏
//     *
//     * @param ctx
//     */
//    public static void collapseStatusBar(Context ctx) {
//        Object sbservice = ctx.getSystemService("statusbar");
//        try {
//            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
//            Method collapse;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                collapse = statusBarManager.getMethod("collapsePanels");
//            } else {
//                collapse = statusBarManager.getMethod("collapse");
//            }
//            collapse.invoke(sbservice);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 展开状态栏
//     *
//     * @param ctx
//     */
//    public static void expandStatusBar(Context ctx) {
//        Object sbservice = ctx.getSystemService("statusbar");
//        try {
//            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
//            Method expand;
//            if (Build.VERSION.SDK_INT >= 17) {
//                expand = statusBarManager.getMethod("expandNotificationsPanel");
//            } else {
//                expand = statusBarManager.getMethod("expand");
//            }
//            expand.invoke(sbservice);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
