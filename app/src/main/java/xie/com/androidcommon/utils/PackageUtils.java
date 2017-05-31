package xie.com.androidcommon.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.net.Uri;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import xie.com.androidcommon.MyApplication;

/**
 * Created by xiechengfa on 2016/9/22.
 * 应用包工具类
 */
public class PackageUtils {
    /**
     * 检查指定包是否已安装
     *
     * @param context     系统变量
     * @param packageName 包名
     */
    public static boolean isAppInstalled(Context context, String packageName) {
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
    public static void installApk(String path) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        // 设置数据类型
        intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        MyApplication.getInstance().startActivity(intent);
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean checkIsServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext.getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            if (myList.get(i).service.getClassName().toString().equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
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