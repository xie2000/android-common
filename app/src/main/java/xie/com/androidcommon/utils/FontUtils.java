package xie.com.androidcommon.utils;

import android.text.TextPaint;
import android.util.TypedValue;

import xie.com.androidcommon.MyApplication;

/**
 * Created by xiechengfa on 2016/9/23
 * 字体处理工具
 */
public class FontUtils {
    /**
     * 计算字宽
     * @param text
     * @param Size
     * @return
     */
    public static float GetTextWidth(String text, float Size) {
        TextPaint FontPaint = new TextPaint();
        FontPaint.setTextSize(Size);
        return FontPaint.measureText(text);
    }

    /**
     * Dip转px
     * @param dip
     * @return
     */
    public static int dip2Px(float dip) {
        //另一种方法：
        //final float scale = context.getResources().getDisplayMetrics().density;
        //return (int) (dipValue * scale + 0.5f);
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, MyApplication.getInstance().getResources().getDisplayMetrics());
    }

    /**
     * px 转 dip
     *
     * @param pxValue
     * @return
     */
    public static int px2Dip(float pxValue) {
        final float scale = MyApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
