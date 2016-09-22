package xie.com.androidcommon.util;

import android.app.Activity;
import android.util.DisplayMetrics;

import xie.com.androidcommon.MyApplication;

/**
 * Created by xiechengfa on 2016/9/22.
 * 工具类
 */
public class Utils {

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
}
