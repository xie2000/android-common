package xie.com.androidcommon.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import xie.com.androidcommon.MyApplication;
import xie.com.androidcommon.R;

/**
 * 图片工具类
 *
 * @author xiechengfa
 */
public class BitmapUtils {
    // 相册里图片的高度的最大像素
    public final static String JPG = ".jpg";
    public final static String JPEG = ".jpeg";
    public final static String PNG = ".png";

    // 图片的方向
    public final static int IMAGE_ORIENTATION_VERTICAL = 1;// 坚屏
    public final static int IMAGE_ORIENTATION_HORIZONTAL = 2;// 横屏
    public final static int IMAGE_ORIENTATION_SQUARE = 3;// 正方

    private static final int COMPRESS_FAIL = 0;
    private static final int COMPRESS_PNG = 1;
    private static final int COMPRESS_JPG = 2;

    /**
     * 创建Drawable
     *
     * @param path
     * @return
     */
    public static Drawable createDrawable(String path) {
        if (path == null) {
            return null;
        }

        Drawable drawable = null;
        try {
            File file = new File(path);
            if (!file.exists() || file.isDirectory()) {
                return null;
            }

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 1;
            opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
            opts.inPurgeable = true;
            opts.inInputShareable = true;
            opts.inDither = false;
            opts.inPurgeable = true;
            FileInputStream is = null;
            Bitmap bitmap = null;
            try {
                is = new FileInputStream(path);
                bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null,
                        opts);
//				if (bitmap == null) {
//					// 创建图片失败，源文件可能有误，则删除
//					FileUtils.deleteFile(path);
//				}
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                        is = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bitmap == null) {
                return null;
            }

            drawable = new BitmapDrawable(MyApplication.getInstance()
                    .getResources(), bitmap);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        return drawable;
    }

    /**
     * 创建Drawable
     *
     * @param resId
     * @return
     */
    public static Drawable createDrawable(int resId) {
        Drawable drawable = null;
        try {
            drawable = MyApplication.getInstance().getResources()
                    .getDrawable(resId);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return drawable;
    }

    /**
     * 创建图片
     *
     * @param path
     * @return
     */
    public static Bitmap createBitmap(String path) {
        if (path == null) {
            return null;
        }

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inDither = false;
        opts.inPurgeable = true;
        FileInputStream is = null;
        Bitmap bitmap = null;
        try {
            is = new FileInputStream(path);
            bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    /**
     * 创建图片
     *
     * @param resId 图片ID
     * @return 图片
     */
    public static Bitmap createBitmap(int resId) {
        return BitmapFactory.decodeResource(MyApplication.getInstance()
                .getResources(), resId);
    }

    /**
     * 创建指定大小的图片
     *
     * @param resId
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createBitmap(int resId, int width, int height) {
        Bitmap resBitmap = null;
        Bitmap srcBimaBitmap = createBitmap(resId);
        if (srcBimaBitmap != null) {
            resBitmap = Bitmap.createScaledBitmap(srcBimaBitmap, width, height,
                    true);
        }
        return resBitmap;
    }

    /**
     * 创建指定大小的图片
     *
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createBitmap(Bitmap bitmap, int width, int height) {
        if (bitmap == null) {
            return null;
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    /**
     * 创建指定大小的图片
     *
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createBitmap(String path, int width, int height) {
        Bitmap resBitmap = null;
        try {
            Bitmap srcBimaBitmap = createBitmap(path);

            if (srcBimaBitmap != null) {
                resBitmap = Bitmap.createScaledBitmap(srcBimaBitmap, width,
                        height, true);
            }

            srcBimaBitmap = null;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return resBitmap;
    }

    /**
     * 按指定大小缩放图片
     *
     * @param srcBitmap
     * @param width     :宽度
     * @param height    :高度
     * @return
     */
    public static Bitmap scaleBitMap(Bitmap srcBitmap, int width, int height) {
        if (srcBitmap == null) {
            return null;
        }

        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createScaledBitmap(srcBitmap, width, height, true);
        } catch (OutOfMemoryError e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * 按比例缩放图片
     *
     * @param srcBitmap
     * @param scaleW    :比例
     * @param scaleH    :比例
     * @return
     */
    public static Bitmap scaleBitMap(Bitmap srcBitmap, float scaleW,
                                     float scaleH) {
        if (srcBitmap == null) {
            return null;
        }

        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createScaledBitmap(srcBitmap,
                    (int) (scaleW * srcBitmap.getWidth()),
                    (int) (scaleH * srcBitmap.getHeight()), true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * 创建图片（类似于centerCrop）
     *
     * @param path
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createFitCropBitmap(String path, int width, int height) {
        if (path == null) {
            return null;
        }

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inDither = false;
        opts.inPurgeable = true;
        FileInputStream is = null;
        Bitmap bitmap = null;
        try {
            is = new FileInputStream(path);
            bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
            bitmap = zoomCropBitmap(bitmap, width, height);
            bitmap = Bitmap.createBitmap(bitmap,
                    (bitmap.getWidth() - width) / 2,
                    (bitmap.getHeight() - height) / 2, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    /**
     * 创建缩放图片
     *
     * @param path
     * @param pixOfWidth
     * @param pixOfHeight
     * @return
     */
    public static Drawable createDrawableOfSampleSize(String path,
                                                      int pixOfWidth, int pixOfHeight) {
        if (path == null) {
            return null;
        }

        if (pixOfWidth * pixOfHeight <= 0) {
            return null;
        }

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
        BitmapFactory.decodeFile(path, opts);

        if (opts.outWidth * opts.outHeight <= 0) {
            return null;
        }

        opts.inSampleSize = computeSampleSize(opts, -1, pixOfWidth
                * pixOfHeight);
        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inDither = false;
        opts.inPurgeable = true;
        FileInputStream is = null;
        Bitmap bitmap = null;
        try {
            is = new FileInputStream(path);
            bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (bitmap == null) {
            return null;
        }

        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    /**
     * 创建缩放图片
     *
     * @param path
     * @param pixOfWidth
     * @param pixOfHeight
     * @return
     */
    public static Bitmap createBitmapOfSampleSize(String path, int pixOfWidth,
                                                  int pixOfHeight) {
        if (path == null) {
            return null;
        }

        if (pixOfWidth * pixOfHeight <= 0) {
            return null;
        }

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
        BitmapFactory.decodeFile(path, opts);

        if (opts.outWidth * opts.outHeight <= 0) {
            return null;
        }

        opts.inSampleSize = computeSampleSize(opts, -1, pixOfWidth
                * pixOfHeight);
        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inDither = false;
        opts.inPurgeable = true;
        FileInputStream is = null;
        Bitmap bitmap = null;
        try {
            is = new FileInputStream(path);
            bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    private static int computeSampleSize(BitmapFactory.Options options,
                                         int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 保存图片
     */
    public static void saveDrawableOfPNG(String path, Drawable drawable) {
        if (drawable == null || path == null) {
            return;
        }

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        saveBitmapOfPNG(path, bitmap);
    }

    /**
     * 保存图片
     *
     * @param path    图片path
     * @param mBitmap 图片
     */
    public static void saveBitmapOfPNG(String path, Bitmap mBitmap) {
        if (mBitmap == null || mBitmap.isRecycled() || path == null) {
            return;
        }

        FileUtils.createFileDir(path);
        File f = new File(path);
        if (f.exists()) {
            return;
        }

        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            mBitmap.compress(CompressFormat.PNG, 100, fOut);
            fOut.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
            FileUtils.deleteFile(path);
        } finally {
            try {
                if (fOut != null)
                    fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存图片
     */
    public static void saveBitmapOfJPG(String path, Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled() || path == null) {
            return;
        }

        FileUtils.createFileDir(path);
        File f = new File(path);
        if (f.exists()) {
            return;
        }

        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            bitmap.compress(CompressFormat.JPEG, 75, fOut);
            fOut.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
            FileUtils.deleteFile(path);
        } finally {
            try {
                if (fOut != null)
                    fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存图片
     */
    public static void saveDrawableOfJPG(String path, Drawable drawable) {
        if (drawable == null || path == null) {
            return;
        }

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        saveBitmapOfJPG(path, bitmap);
    }

    /**
     * 保存图片
     */
    public static void saveBitmapOfJPG100(String path, Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled() || path == null) {
            return;
        }

        FileUtils.createFileDir(path);
        File f = new File(path);
        if (f.exists()) {
            return;
        }

        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            bitmap.compress(CompressFormat.JPEG, 100, fOut);
            fOut.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
            FileUtils.deleteFile(path);
        } finally {
            try {
                if (fOut != null)
                    fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * bitmap转换byte
     *
     * @param bmp
     * @return
     */
    public static byte[] readBitmapToByte(final Bitmap bmp) {
        if (bmp == null) {
            return null;
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        bmp.recycle();

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * @param @param  path
     * @param @return
     * @return Point
     * @Desp:获取图片宽高
     */
    public static Point getImageSize(String path) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
        BitmapFactory.decodeFile(path, opts);
        return new Point(opts.outWidth, opts.outHeight);
    }

    /**
     * @param @param  path
     * @param @return
     * @return Point
     * @Desp:获取图片宽
     */
    public static int getImageWidth(String path) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
        BitmapFactory.decodeFile(path, opts);
        return opts.outWidth;
    }

    /**
     * @param @param  path
     * @param @return
     * @return Point
     * @Desp:获取图片高
     */
    public static int getImageHeight(String path) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
        BitmapFactory.decodeFile(path, opts);
        return opts.outHeight;
    }

    /**
     * @param @param  path
     * @param @return
     * @return Point
     * @Desp:获取图片宽高
     */
    public static Point getImageSize(int resId) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
        BitmapFactory.decodeResource(MyApplication.getInstance()
                .getResources(), resId, opts);
        return new Point(opts.outWidth, opts.outHeight);
    }

    /**
     * 获取图片的宽
     *
     * @param resId
     * @return
     */
    public static int getImageWidth(int resId) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
        BitmapFactory.decodeResource(MyApplication.getInstance()
                .getResources(), resId, opts);
        return opts.outWidth;
    }

    /**
     * 获取图片的高
     *
     * @param resId
     * @return
     */
    public static int getImageHeight(int resId) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
        BitmapFactory.decodeResource(MyApplication.getInstance()
                .getResources(), resId, opts);
        return opts.outHeight;
    }

    /**
     * 更改drawable大小(比例不变)
     *
     * @param drawable
     * @param w
     * @param h
     * @return
     */
    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        float width = drawable.getIntrinsicWidth();
        float height = drawable.getIntrinsicHeight();
        float myScale = w / width;

        float newHeight = height * myScale;
        Bitmap newbmp = null;
        if (newHeight > h) {
            myScale = h / height;
            newbmp = Bitmap.createScaledBitmap(drawableToBitmap(drawable),
                    (int) (width * myScale), h, true);
        } else {
            newbmp = Bitmap.createScaledBitmap(drawableToBitmap(drawable), w,
                    (int) (height * myScale), true);
        }

        return new BitmapDrawable(MyApplication.getInstance().getResources(),
                newbmp);
    }

    /**
     * 更改bitmap大小(比例不变)
     *
     * @param w
     * @param h
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        float myScale = w / width;

        float newHeight = height * myScale;
        Bitmap newbmp = null;
        if (newHeight > h) {
            myScale = h / height;
            newbmp = Bitmap.createScaledBitmap(bitmap, (int) (width * myScale),
                    h, true);
        } else {
            newbmp = Bitmap.createScaledBitmap(bitmap, w,
                    (int) (height * myScale), true);
        }

        return newbmp;
    }

    /**
     * 更改bitmap大小(比例不变)
     *
     * @param w
     * @param h
     * @return
     */
    public static Bitmap zoomCropBitmap(Bitmap bitmap, int w, int h) {
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        float myScale = 0;
        if (w > h) {
            myScale = w / width;
        } else {
            myScale = h / height;
        }

        float newHeight = height * myScale;
        float newWidth = width * myScale;

        Bitmap newbmp = null;
        newbmp = Bitmap.createScaledBitmap(bitmap, (int) newWidth,
                (int) newHeight, true);

        return newbmp;
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bm = bd.getBitmap();
        return bm;
    }

    /**
     * 获取图片的方向
     *
     * @param bitmap
     * @return
     */
    public static int getImageOrientationOfBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return IMAGE_ORIENTATION_SQUARE;
        }

        if (bitmap.getHeight() < bitmap.getWidth()) {
            return IMAGE_ORIENTATION_VERTICAL;
        } else if (bitmap.getWidth() > bitmap.getHeight()) {
            return IMAGE_ORIENTATION_HORIZONTAL;
        } else {
            return IMAGE_ORIENTATION_SQUARE;
        }
    }

    /**
     * 获取图片的方向
     *
     * @return
     */
    public static int getImageOrientationOfDrawable(Drawable drawable) {
        if (drawable == null) {
            return IMAGE_ORIENTATION_SQUARE;
        }

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        if (bitmap == null) {
            return IMAGE_ORIENTATION_SQUARE;
        }

        if (bitmap.getHeight() > bitmap.getWidth()) {
            return IMAGE_ORIENTATION_VERTICAL;
        } else if (bitmap.getWidth() > bitmap.getHeight()) {
            return IMAGE_ORIENTATION_HORIZONTAL;
        } else {
            return IMAGE_ORIENTATION_SQUARE;
        }
    }

    /**
     * 获取指定数量的水平平铺的图片
     *
     * @return
     */
    public static Bitmap getNumBitmapHorizontal(Bitmap resBitmap, int num) {
        if (num == 0 || resBitmap == null) {
            return null;
        }

        Bitmap dstBitmap = null;
        try {
            int bitmapWidth = resBitmap.getWidth();
            int bitmapHeight = resBitmap.getHeight();
            dstBitmap = Bitmap.createBitmap(bitmapWidth * num, bitmapHeight,
                    Config.ARGB_8888);
            Canvas canvas = new Canvas(dstBitmap);
            Rect src = new Rect();
            Rect dst = new Rect();

            for (int i = 0; i < num; i++) {
                src.set(0, 0, bitmapWidth, bitmapHeight);
                dst.set(i * bitmapWidth, 0, i * bitmapWidth + bitmapWidth,
                        bitmapHeight);
                canvas.drawBitmap(resBitmap, src, dst, null);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return dstBitmap;
    }

    /**
     * 返回对应数字的图片
     *
     * @param resId
     * @param numStr
     * @return
     */
    public static Bitmap getBitmapofNum(int resId, String numStr) {
        if (numStr == null || numStr.trim().length() <= 0) {
            return null;
        }

        Bitmap dstBitmap = null;
        try {
            Bitmap resBitmap = createBitmap(resId);
            int v = resBitmap.getWidth() % 10;
            // 修正图片宽度象素为可被10整除，为了避免截取图片位置出错的问题
            if (v != 0) {
                if (v < 5) {
                    int w1 = resBitmap.getWidth() - v;
                    resBitmap = Bitmap.createScaledBitmap(resBitmap, w1,
                            resBitmap.getHeight(), true);
                } else {
                    int w2 = resBitmap.getWidth() + 10 - v;
                    resBitmap = Bitmap.createScaledBitmap(resBitmap, w2,
                            resBitmap.getHeight(), true);
                }
            }

            int numWidth = resBitmap.getWidth() / 10;
            int numHeight = resBitmap.getHeight();
            dstBitmap = Bitmap.createBitmap(numWidth * numStr.length(),
                    numHeight, Config.ARGB_8888);
            Canvas canvas = new Canvas(dstBitmap);
            Rect src = new Rect();
            Rect dst = new Rect();

            for (int i = 0; i < numStr.length(); i++) {
                int num = Integer.parseInt(numStr.charAt(i) + "");
                src.set(num * numWidth, 0, num * numWidth + numWidth, numHeight);
                dst.set(i * numWidth, 0, i * numWidth + numWidth, numHeight);
                canvas.drawBitmap(resBitmap, src, dst, null);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return dstBitmap;
    }

    /**
     * 返回对应数字的图片
     *
     * @param resId
     * @param numStr
     * @return
     */
    public static Drawable getDrawablepofNum(int resId, String numStr) {
        Bitmap bitmap = getBitmapofNum(resId, numStr);
        if (bitmap == null) {
            return null;
        }

        Drawable drawable = new BitmapDrawable(MyApplication.getInstance()
                .getResources(), bitmap);
        return drawable;
    }

    /**
     * 通过资源图片的名称创建图片
     *
     * @param resName
     * @return
     */
    public static Drawable getDrawableOfResName(String resName) {
        return MyApplication.getInstance().getResources()
                .getDrawable(getDrawableResIdOfResName(resName));
    }

    /**
     * 通过资源图片的名称返回资源图片ID
     *
     * @param resName
     * @return
     */
    public static int getDrawableResIdOfResName(String resName) {
        if (resName == null || resName.trim().length() <= 0) {
            return 0;
        }

        int resId = 0;
        try {
            Class drawableClass = R.drawable.class;
            Field field = null;
            field = drawableClass.getField(resName);
            resId = field.getInt(field.getName());
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return resId;
    }

    // 将图片旋转
    private static void revolvePic(File saveFile, String orgPath,
                                   String tempPath, int compressRes, FileOutputStream fOut2)
            throws IOException {
        File file = new File(orgPath);
        // 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
        int degree = readBitmapDegree(file.getAbsolutePath());
        // 把压缩后的图片旋转为正的方向
        Bitmap tempBitmap = BitmapFactory.decodeFile(tempPath);
        Bitmap saveBitmap = rotate(degree, tempBitmap);
        saveFile.createNewFile();
        fOut2 = new FileOutputStream(saveFile);
        if (compressRes == COMPRESS_PNG) {
            saveBitmap.compress(CompressFormat.PNG, 30, fOut2);
        } else if (compressRes == COMPRESS_JPG) {
            saveBitmap.compress(CompressFormat.JPEG, 30, fOut2);
        }
        if (tempBitmap != null) {
            tempBitmap.recycle();
            tempBitmap = null;
        }
        if (saveBitmap != null) {
            saveBitmap.recycle();
            saveBitmap = null;
        }
        System.gc();
    }

    // 读取图像的旋转度
    public static int readBitmapDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return
     */
    public static Bitmap rotate(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        return Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    /**
     * 水平翻转图片
     *
     * @param bitmap 原图
     * @return 水平翻转后的图片
     */
    public static Bitmap reverseByHorizontal(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, false);
    }

    /**
     * 垂直翻转图片
     *
     * @param bitmap 原图
     * @return 垂直翻转后的图片
     */
    public static Bitmap reverseByVertical(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, false);
    }


    // 计算图片的缩放值
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 从View获取Bitmap
     *
     * @param view View
     * @return Bitmap
     */
    public static Bitmap getBitmapFromView(View view) {
        if (view == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        view.layout(view.getLeft(), view.getTop(), view.getRight(),
                view.getBottom());
        view.draw(canvas);

        return bitmap;
    }

    /**
     * 把一个View的对象转换成bitmap
     *
     * @param view View
     * @return Bitmap
     */
    public static Bitmap getBitmapFromView2(View view) {
        if (view == null) {
            return null;
        }
        view.clearFocus();
        view.setPressed(false);

        // 能画缓存就返回false
        boolean willNotCache = view.willNotCacheDrawing();
        view.setWillNotCacheDrawing(false);
        int color = view.getDrawingCacheBackgroundColor();
        view.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            view.destroyDrawingCache();
        }
        view.buildDrawingCache();
        Bitmap cacheBitmap = view.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        // Restore the view
        view.destroyDrawingCache();
        view.setWillNotCacheDrawing(willNotCache);
        view.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }

    /**
     * 合并Bitmap
     *
     * @param bgd 背景Bitmap
     * @param fg  前景Bitmap
     * @return 合成后的Bitmap
     */
    public static Bitmap combineImages(Bitmap bgd, Bitmap fg) {
        Bitmap bmp;

        int width = bgd.getWidth() > fg.getWidth() ? bgd.getWidth() : fg
                .getWidth();
        int height = bgd.getHeight() > fg.getHeight() ? bgd.getHeight() : fg
                .getHeight();

        bmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(bgd, 0, 0, null);
        canvas.drawBitmap(fg, 0, 0, paint);

        return bmp;
    }

    /**
     * 合并
     *
     * @param bgd 后景Bitmap
     * @param fg  前景Bitmap
     * @return 合成后Bitmap
     */
    public static Bitmap combineImagesToSameSize(Bitmap bgd, Bitmap fg) {
        Bitmap bmp;

        int width = bgd.getWidth() < fg.getWidth() ? bgd.getWidth() : fg
                .getWidth();
        int height = bgd.getHeight() < fg.getHeight() ? bgd.getHeight() : fg
                .getHeight();

        if (fg.getWidth() != width && fg.getHeight() != height) {
            fg = zoom(fg, width, height);
        }
        if (bgd.getWidth() != width && bgd.getHeight() != height) {
            bgd = zoom(bgd, width, height);
        }

        bmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(bgd, 0, 0, null);
        canvas.drawBitmap(fg, 0, 0, paint);

        return bmp;
    }

    /**
     * 放大缩小图片
     *
     * @param bitmap 源Bitmap
     * @param w      宽
     * @param h      高
     * @return 目标Bitmap
     */
    public static Bitmap zoom(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidht = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidht, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newbmp;
    }

    /**
     * 获得圆角的Bitmap
     *
     * @param bitmap  源Bitmap
     * @param roundPx 圆角大小
     * @return 期望Bitmap
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 获得带倒影的Bitmap
     *
     * @param bitmap 源Bitmap
     * @return 带倒影的Bitmap
     */
    public static Bitmap createReflectionBitmap(Bitmap bitmap) {
        final int reflectionGap = 4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
                width, height / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
                (height + height / 2), Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
    }

    /**
     * 压缩图片大小
     *
     * @param image 源Bitmap
     * @return 压缩后的Bitmap
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 将彩色图转换为灰度图
     *
     * @param img 源Bitmap
     * @return 返回转换好的位图
     */
    public static Bitmap convertGreyImg(Bitmap img) {
        int width = img.getWidth(); // 获取位图的宽
        int height = img.getHeight(); // 获取位图的高

        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

        img.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap result = Bitmap.createBitmap(width, height, Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }

    /**
     * 获得圆形的Bitmap
     *
     * @param bitmap 传入Bitmap对象
     * @return 圆形Bitmap
     */
    public static Bitmap getRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * 缩略图
     * @param bitmap  The bitmap to get a thumbnail of.
     * @param context The application's context.
     * @return A thumbnail for the specified bitmap or the bitmap itself if the
     * thumbnail could not be created.
     */
    public static Bitmap createThumbnailBitmap(Bitmap bitmap, Context context) {
        int sIconWidth = -1;
        int sIconHeight = -1;
        final Resources resources = context.getResources();
        sIconWidth = sIconHeight = (int) resources
                .getDimension(android.R.dimen.app_icon_size);

        final Paint sPaint = new Paint();
        final Rect sBounds = new Rect();
        final Rect sOldBounds = new Rect();
        Canvas sCanvas = new Canvas();

        int width = sIconWidth;
        int height = sIconHeight;

        sCanvas.setDrawFilter(new PaintFlagsDrawFilter(Paint.DITHER_FLAG,
                Paint.FILTER_BITMAP_FLAG));

        final int bitmapWidth = bitmap.getWidth();
        final int bitmapHeight = bitmap.getHeight();

        if (width > 0 && height > 0) {
            if (width < bitmapWidth || height < bitmapHeight) {
                final float ratio = (float) bitmapWidth / bitmapHeight;

                if (bitmapWidth > bitmapHeight) {
                    height = (int) (width / ratio);
                } else if (bitmapHeight > bitmapWidth) {
                    width = (int) (height * ratio);
                }

                final Config c = (width == sIconWidth && height == sIconHeight) ? bitmap
                        .getConfig() : Config.ARGB_8888;
                final Bitmap thumb = Bitmap.createBitmap(sIconWidth,
                        sIconHeight, c);
                final Canvas canvas = sCanvas;
                final Paint paint = sPaint;
                canvas.setBitmap(thumb);
                paint.setDither(false);
                paint.setFilterBitmap(true);
                sBounds.set((sIconWidth - width) / 2,
                        (sIconHeight - height) / 2, width, height);
                sOldBounds.set(0, 0, bitmapWidth, bitmapHeight);
                canvas.drawBitmap(bitmap, sOldBounds, sBounds, paint);
                return thumb;
            } else if (bitmapWidth < width || bitmapHeight < height) {
                final Config c = Config.ARGB_8888;
                final Bitmap thumb = Bitmap.createBitmap(sIconWidth,
                        sIconHeight, c);
                final Canvas canvas = sCanvas;
                final Paint paint = sPaint;
                canvas.setBitmap(thumb);
                paint.setDither(false);
                paint.setFilterBitmap(true);
                canvas.drawBitmap(bitmap, (sIconWidth - bitmapWidth) / 2,
                        (sIconHeight - bitmapHeight) / 2, paint);
                return thumb;
            }
        }

        return bitmap;
    }

    /**
     * 生成水印图片 水印在右下角
     *
     * @param src       the bitmap object you want proecss
     * @param watermark the water mark above the src
     * @return return a bitmap object ,if paramter's length is 0,return null
     */
    public static Bitmap createWatermarkBitmap(Bitmap src, Bitmap watermark) {
        if (src == null) {
            return null;
        }

        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        // create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        // draw src into
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        // draw watermark into
        cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 在src的右下角画入水印
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        // store
        cv.restore();// 存储
        return newb;
    }

    /**
     * 图片压缩处理（使用compress的方法）
     * <p/>
     * <br>
     * <b>说明</b> 如果bitmap本身的大小小于maxSize，则不作处理
     *
     * @param bitmap  要压缩的图片
     * @param maxSize 压缩后的大小，单位kb
     */
    public static void compress(Bitmap bitmap, double maxSize) {
        // 将bitmap放至数组中，意在获得bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 格式、质量、输出流
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] b = baos.toByteArray();
        // 将字节换成KB
        double mid = b.length / 1024;
        // 获取bitmap大小 是允许最大大小的多少倍
        double i = mid / maxSize;
        // 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
        if (i > 1) {
            // 缩放图片 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
            // （保持宽高不变，缩放后也达到了最大占用空间的大小）
            bitmap = scale(bitmap, bitmap.getWidth() / Math.sqrt(i),
                    bitmap.getHeight() / Math.sqrt(i));
        }
    }

    /**
     * 图片的缩放方法
     *
     * @param src       ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     */
    public static Bitmap scale(Bitmap src, double newWidth, double newHeight) {
        // 记录src的宽高
        float width = src.getWidth();
        float height = src.getHeight();
        // 创建一个matrix容器
        Matrix matrix = new Matrix();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 开始缩放
        matrix.postScale(scaleWidth, scaleHeight);
        // 创建缩放后的图片
        return Bitmap.createBitmap(src, 0, 0, (int) width, (int) height,
                matrix, true);
    }
}
