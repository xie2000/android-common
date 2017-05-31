package xie.com.androidcommon.utils;

/**
 * 此内用于框架系统打印输出控制，使用者用XLog格式化体验更好。
 *
 */
public class XPrintUtils {

    private static String tag = "AndroidCommon";
    private static boolean isDebug = true;

    public static void setIsDebug(boolean isDebug) {
        XPrintUtils.isDebug = isDebug;
    }

    public static void i(String msg) {
        if (isDebug)
            android.util.Log.i(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (isDebug)
            android.util.Log.i(tag, msg);
    }

    public static void d(String msg) {
        if (isDebug)
            android.util.Log.d(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            android.util.Log.d(tag, msg);
    }

    public static void w(String msg) {
        if (isDebug)
            android.util.Log.w(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (isDebug)
            android.util.Log.w(tag, msg);
    }

    public static void v(String msg) {
        if (isDebug)
            android.util.Log.v(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            android.util.Log.v(tag, msg);
    }

    public static void e(String msg) {
        android.util.Log.e(tag, msg);
    }

    public static void e(String tag, String msg) {
        android.util.Log.e(tag, msg);
    }
}
