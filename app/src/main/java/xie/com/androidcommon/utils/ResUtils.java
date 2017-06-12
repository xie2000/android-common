package xie.com.androidcommon.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

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
}
