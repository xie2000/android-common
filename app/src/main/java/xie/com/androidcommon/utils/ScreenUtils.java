package xie.com.androidcommon.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.lang.reflect.Method;

import xie.com.androidcommon.MyApplication;

/**
 * 获得屏幕相关的辅助类
 * 
 * @author zhy
 * 
 */
public class ScreenUtils
{
	/**
	 * 获得屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context)
	{
		return MyApplication.getDisplayMetrics().widthPixels;
	}

	/**
	 * 获得屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context)
	{
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
		Resources resources = ResUtils.getRes();
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
		Resources resources = ResUtils.getRes();
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

	/**
	 * 获取当前屏幕截图，包含状态栏
	 * 
	 * @param activity
	 * @return
	 */
	public static Bitmap snapShotWithStatusBar(Activity activity)
	{
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
		view.destroyDrawingCache();
		return bp;

	}

	/**
	 * 获取当前屏幕截图，不包含状态栏
	 * 
	 * @param activity
	 * @return
	 */
	public static Bitmap snapShotWithoutStatusBar(Activity activity)
	{
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
				- statusBarHeight);
		view.destroyDrawingCache();
		return bp;
	}
}
