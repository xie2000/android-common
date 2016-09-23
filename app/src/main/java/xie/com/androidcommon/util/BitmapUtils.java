package xie.com.androidcommon.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.util.Size;
import android.widget.ImageView;

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
 * 
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
	 * @param resId
	 *            图片ID
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
	 * @param width
	 *            :宽度
	 * @param height
	 *            :高度
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
	 * @param scaleW
	 *            :比例
	 * @param scaleH
	 *            :比例
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
	 * @param path
	 *            图片path
	 * @param mBitmap
	 *            图片
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
	 * @Desp:获取图片宽高
	 * @param @param path
	 * @param @return
	 * @return Point
	 */
	public static Point getImageSize(String path) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
		BitmapFactory.decodeFile(path, opts);
		return new Point(opts.outWidth, opts.outHeight);
	}

	/**
	 * @Desp:获取图片宽
	 * @param @param path
	 * @param @return
	 * @return Point
	 */
	public static int getImageWidth(String path) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
		BitmapFactory.decodeFile(path, opts);
		return opts.outWidth;
	}

	/**
	 * @Desp:获取图片高
	 * @param @param path
	 * @param @return
	 * @return Point
	 */
	public static int getImageHeight(String path) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
		BitmapFactory.decodeFile(path, opts);
		return opts.outHeight;
	}

	/**
	 * @Desp:获取图片宽高
	 * @param @param path
	 * @param @return
	 * @return Point
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
		Bitmap saveBitmap = rotaingBitmap(degree, tempBitmap);
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

	// 旋转图片
	public static Bitmap rotaingBitmap(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, false);
		return resizedBitmap;
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
}
