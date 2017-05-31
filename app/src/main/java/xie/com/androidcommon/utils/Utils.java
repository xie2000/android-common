package xie.com.androidcommon.utils;

import android.graphics.Bitmap;

/**
 * Created by xiechengfa on 2016/9/22.
 * 工具类
 */
public class Utils {
    /**
     * 回收图片
     *
     * @param bitmap
     */
    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }

        // 如果图片还未回收，先强制回收该图片
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    /**
     * 批量回收
     *
     * @param bitmap
     */
    public static void reycleBitmapArr(Bitmap[] bitmap) {
        if (bitmap != null) {
            for (int i = 0; i < bitmap.length; i++) {
                recycleBitmap(bitmap[i]);
            }
        }
        bitmap = null;
    }
}
