package xie.com.androidcommon.util;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import xie.com.androidcommon.MyApplication;

/**
 * Created by xiechengfa on 2016/9/22.
 * 读取资源文件定义的数据
 */
public class ResUtil {
    /**
     * 获取资源文件里的dimension(像素,效果同getResDimensionPixelSize（）)
     *
     * @param resId
     * @return
     */
    public static int getResDimensionPixelSize(int resId) {
        return MyApplication.getInstance().getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获取资源文件里的dimension（像素）
     *
     * @param resId
     * @return
     */
    public static float getResDimension(int resId) {
        return MyApplication.getInstance().getResources().getDimension(resId);
    }

    /**
     * 获取资源文件里的color
     *
     * @param resId
     * @return
     */
    public static int getResColor(int resId) {
        return MyApplication.getInstance().getResources().getColor(resId);
    }

    /**
     * 获取资源文件里的字符串
     *
     * @param resId
     * @return
     */
    public static String getResString(int resId) {
        return MyApplication.getInstance().getResources().getString(resId);
    }

    /**
     * 获取资源文件里的字符串数组
     *
     * @param resId
     * @return
     */
    public static String[] getResArrString(int resId) {
        return MyApplication.getInstance().getResources().getStringArray(resId);
    }

    /**
     * 获取资源文件里的integer
     *
     * @param resId
     * @return
     */
    public static int getResInteger(int resId) {
        return MyApplication.getInstance().getResources().getInteger(resId);
    }

    /**
     * 获取资源文件里的图片
     */
    public static Drawable getResDrawable(int resId) {
        return MyApplication.getInstance().getResources().getDrawable(resId);
    }

    /**
     * 获取资源管理
     */
    public static Resources getRes() {
        return MyApplication.getInstance().getResources();
    }

}
