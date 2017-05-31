package xie.com.androidcommon.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.lang.reflect.Method;

import xie.com.androidcommon.MyApplication;

/**
 * Created by xiechengfa on 2016/9/22.
 * 读取资源文件定义的数据
 */
public class ResUtils {

    /**
     * 获取资源文件里的dimension(像素,效果同getResDimensionPixelSize（）)
     *
     * @param resId
     * @return
     */
    public static int getResDimensionPixelSize(int resId) {
        return getRes().getDimensionPixelSize(resId);
    }

    /**
     * 获取资源文件里的dimension（像素）
     *
     * @param resId
     * @return
     */
    public static float getResDimension(int resId) {
        return getRes().getDimension(resId);
    }

    /**
     * 获取资源文件里的color
     *
     * @param resId
     * @return
     */
    public static int getResColor(int resId) {
        return ContextCompat.getColor(MyApplication.getInstance().getApplicationContext(), resId);
    }

    /**
     * 获取资源文件里的字符串
     *
     * @param resId
     * @return
     */
    public static String getResString(int resId) {
        return getRes().getString(resId);
    }

    /**
     * 获取资源文件里的字符串数组
     *
     * @param resId
     * @return
     */
    public static String[] getResArrString(int resId) {
        return getRes().getStringArray(resId);
    }

    /**
     * 获取资源文件里的integer
     *
     * @param resId
     * @return
     */
    public static int getResInteger(int resId) {
        return getRes().getInteger(resId);
    }

    /**
     * 获取资源文件里的图片
     */
    public static Drawable getResDrawable(int resId) {
        return ContextCompat.getDrawable(MyApplication.getInstance().getApplicationContext(), resId);
    }

    public static int getResIdByStr(String dir, String strPre, int id) {
        if (id >= 0) {
            String sItemId = strPre + id;
            try {
                return getRes().getIdentifier(sItemId, dir, MyApplication.getInstance().getPackageName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return 0;
    }

    public static int getResIdByStr(String dir, String strPre, String postfix) {
        if (!TextUtils.isEmpty(postfix)) {
            String sItemId = strPre + postfix;
            try {
                return getRes().getIdentifier(sItemId, dir, MyApplication.getInstance().getPackageName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return 0;
    }

    /**
     * 获取资源管理
     */
    public static Resources getRes() {
        return MyApplication.getInstance().getApplicationContext().getResources();
    }

    /**
     * 获取meta的数值
     *
     * @param metaKey
     * @return
     */
    public static String getMetaValue(String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (metaKey == null) {
            return null;
        }

        try {

            ApplicationInfo ai = MyApplication.getInstance().getPackageManager().getApplicationInfo(MyApplication.getInstance().getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }

        return apiKey;
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(float pxValue) {
        final float scale = getRes().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(float dipValue) {
        final float scale = getRes().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(float pxValue) {
        final float fontScale = getRes().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = getRes().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕的宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        return MyApplication.getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕的高度
     *
     * @return
     */
    public static int getScreenHeight() {
        return MyApplication.getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕真正的高度
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int getScreenRealHeight() {
        int h;
        WindowManager winMgr = (WindowManager) MyApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        Display display = winMgr.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealMetrics(dm);
            h = dm.heightPixels;
        } else {
            try {
                Method method = Class.forName("android.view.Display").getMethod("getRealMetrics", DisplayMetrics.class);
                method.invoke(display, dm);
                h = dm.heightPixels;
            } catch (Exception e) {
                display.getMetrics(dm);
                h = dm.heightPixels;
            }
        }
        return h;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight() {
        int result = 0;

        int resourceId = ResUtils.getRes().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = ResUtils.getResDimensionPixelSize(resourceId);
        }

        return result;
    }

    /**
     * 获取底部导航栏高度
     *
     * @return
     */
    public static int getNavigationBarHeight() {
        int navigationBarHeight = 0;
        Resources resources = getRes();
        int resourceId = resources.getIdentifier(resources.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape", "dimen", "android");
        if (resourceId > 0 && checkDeviceHasNavigationBar()) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId);
        }
        return navigationBarHeight;
    }

    /**
     * 检测是否具有底部导航栏。（一加手机上判断不准确，希望有猿友能修复这个 bug）
     *
     * @return
     */
    public static boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources resources = getRes();
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = resources.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }

    /**
     * 在onCreate中获取视图的尺寸
     * <p>需回调onGetSizeListener接口，在onGetSize中获取view宽高</p>
     * <p>用法示例如下所示</p>
     * <pre>
     * SizeUtils.forceGetViewSize(view, new SizeUtils.onGetSizeListener() {
     *     Override
     *     public void onGetSize(View view) {
     *         view.getWidth();
     *     }
     * });
     * </pre>
     *
     * @param view     视图
     * @param listener 监听器
     */
    public static void forceGetViewSize(final View view, final onGetSizeListener listener) {
        view.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onGetSize(view);
                }
            }
        });
    }

    /**
     * 获取到View尺寸的监听
     */
    public interface onGetSizeListener {
        void onGetSize(View view);
    }

    public static void setListener(onGetSizeListener listener) {
        mListener = listener;
    }

    private static onGetSizeListener mListener;

    /**
     * 测量视图尺寸
     *
     * @param view 视图
     * @return arr[0]: 视图宽度, arr[1]: 视图高度
     */
    public static int[] measureView(View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
        int widthSpec = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
        int lpHeight = lp.height;
        int heightSpec;
        if (lpHeight > 0) {
            heightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
        } else {
            heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        view.measure(widthSpec, heightSpec);
        return new int[]{view.getMeasuredWidth(), view.getMeasuredHeight()};
    }

    /**
     * 获取测量视图宽度
     *
     * @param view 视图
     * @return 视图宽度
     */
    public static int getMeasuredWidth(View view) {
        return measureView(view)[0];
    }

    /**
     * 获取测量视图高度
     *
     * @param view 视图
     * @return 视图高度
     */
    public static int getMeasuredHeight(View view) {
        return measureView(view)[1];
    }
}
