package xie.com.androidcommon.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 图片数字解析器
 * resBitmap是0-9的10张数据图片，返回多个数字拼成的图片
 * 两种方式返回，1.以图片的形式返回；2.绘制到提供的Canvas上
 * @author xiechengfa
 * 
 */
public class BitmapNumberParser {
	private Bitmap[] resBitmap = null; // 数字资源文件
	private int numBitmapWidth = 0;// 数字的宽度
	private int numBitmapHeight = 0;// 数字的高度
	private String numStr = null;

	/**
	 * 初始化数字资源图片
	 * 
	 * @param resBitmap
	 *            资源图片
	 */
	public void initResBitmap(Bitmap[] resBitmap) {
		this.resBitmap = resBitmap;
		numBitmapWidth = resBitmap[0].getWidth();
		numBitmapHeight = resBitmap[0].getHeight();
	}

	/**
	 * 获取对应数字的图片
	 * 
	 * @param num
	 *            数字
	 * @return 数字图片
	 */
	public Bitmap getNumOfBitmap(int num) {
		if (resBitmap == null) {
			return null;
		}

		String tempNum = "" + num;
		Bitmap bitmap = Bitmap.createBitmap(numBitmapWidth * tempNum.length(),
				numBitmapHeight, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		for (int i = 0; i < tempNum.length(); i++) {
			canvas.drawBitmap(
					resBitmap[Integer.parseInt("" + tempNum.charAt(i))], i
							* numBitmapWidth, 0, null);
		}
		return bitmap;
	}

	/**
	 * 绘制到相应的Canvas上
	 * 
	 * @param canvas
	 * @param num
	 * @param x
	 * @param y
	 * @param paint
	 */
	public void drawNum(Canvas canvas, int num, int x, int y, Paint paint) {
		if (resBitmap == null) {
			return;
		}
		numStr = "" + num;
		for (int i = 0; i < numStr.length(); i++) {
			canvas.drawBitmap(
					resBitmap[Integer.parseInt("" + numStr.charAt(i))], x + i
							* numBitmapWidth, y, null);
		}
		numStr = null;
	}

	/**
	 * 获取数字的宽度
	 * 
	 * @return
	 */
	public int getNumWidth() {
		return numBitmapWidth;
	}

	/**
	 * 获取数字的高度
	 * 
	 * @return
	 */
	public int getNumHeight() {
		return numBitmapHeight;
	}

	/**
	 * 回收资源
	 */
	public void recycleRes() {
		numStr = null;

		if (resBitmap != null) {
			Utils.reycleBitmapArr(resBitmap);
		}
	}
}
