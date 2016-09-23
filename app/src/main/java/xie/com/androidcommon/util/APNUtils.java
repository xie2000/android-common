package xie.com.androidcommon.util;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import xie.com.androidcommon.MyApplication;

/**
 * @author xiechengfa
 * @version 创建时间：2012-6-13 上午11:18:43 类说明 :
 */
public class APNUtils {
	// wifi
	public static String WIFI = "wifi";
	// 电信ctwap
	public static String CTWAP = "ctwap";
	// 电信ctnet
	public static String CTNET = "ctnet";
	// 中国移动cmwap
	public static String CMWAP = "cmwap";
	// 中国移动cmnet
	public static String CMNET = "cmnet";
	// 3G wap 中国联通3gwap APN
	public static String GWAP_3 = "3gwap";
	// uni wap 中国联通uni wap APN
	public static String UNIWAP = "uniwap";
	// 3G net 中国联通3gnet APN
	public static String GNET_3 = "3gnet";
	// uni net 中国联通uni net APN
	public static String UNINET = "uninet";

	// 电信3G的类型
	public static String CT_3G_TYPE = "#777";

	// 获取网络类型
	public static String getNetworkType() {
		String apn = null;
		// 连接管理
		ConnectivityManager manager = (ConnectivityManager) MyApplication
				.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
		// 网络信息
		NetworkInfo info = manager.getActiveNetworkInfo();
		// apn信息
		if (info != null) {
			apn = info.getExtraInfo();
			if (apn == null) {
				apn = WIFI;
			}
		} else {
			apn = "no apn";
		}

		if (CT_3G_TYPE.equals(apn)) {
			apn = getCT3GApnType();
		}

		if (apn == null) {
			apn = "wifi";
		}

		return apn;
	}

	private static String getCT3GApnType() {
		String apntype = "nomatch";
		Cursor c = MyApplication
				.getInstance()
				.getContentResolver()
				.query(Uri.parse("content://telephony/carriers/preferapn"),
						null, null, null, null);
		c.moveToFirst();
		String user = c.getString(c.getColumnIndex("user"));
		if (user.startsWith(CTNET)) {
			apntype = CTNET;
		} else if (user.startsWith(CTWAP)) {
			apntype = CTWAP;
		}
		return apntype;
	}

	/**
	 * 是否为电信3G网络
	 * 
	 * @return
	 */
	public static boolean isCT3G() {
		String apn = getNetworkType();
		return apn != null && (apn.equals(CTNET) || apn.equals(CTWAP));
	}

	/**
	 * 是否为联通WAP网络
	 * 
	 * @return
	 */
	public static boolean isWapNetworkOfUnicom() {
		String apn = getNetworkType();
		return (GWAP_3.equals(apn) || UNIWAP.equals(apn));
	}

	/**
	 * 是否为联通网络
	 * 
	 * @return
	 */
	public static boolean isUnicomNetWork() {
		String apn = getNetworkType();
		return (GWAP_3.equals(apn) || UNIWAP.equals(apn) || GNET_3.equals(apn) || UNINET
				.equals(apn));
	}

	/**
	 * 是否为WAP网络
	 * 
	 * @return
	 */
	public static boolean isWapNetwork() {
		String apn = getNetworkType();
		return (CMWAP.equals(apn) || GWAP_3.equals(apn) || UNIWAP.equals(apn) || CTWAP
				.equals(apn));
	}

	/**
	 * 是否为电信wap
	 */
	public static boolean isWapNetworkOfCT() {
		String apn = getNetworkType();
		return (apn != null && apn.equals(CTWAP));
	}

	/**
	 * 是否为WIFI网络
	 * 
	 * @return
	 */
	public static boolean isWIFINetwork() {
		ConnectivityManager manager = (ConnectivityManager) MyApplication
				.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return false;
		}

		NetworkInfo ni = manager.getActiveNetworkInfo();
		if (ni == null) {
			return false;
		}

		if (ni.getState() == NetworkInfo.State.CONNECTED
				|| ni.getState() == NetworkInfo.State.CONNECTING) {
			if (ni.getType() == ConnectivityManager.TYPE_WIFI) {
				return true;// WIFI
			}
		}

		return false;
	}
}
