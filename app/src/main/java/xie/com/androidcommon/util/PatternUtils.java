package xie.com.androidcommon.util;

import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 */
public class PatternUtils {
//	// email 正则表达式
//	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z]{2,}){1}$)";
//
//	// password正则表达式
//	public final static String PASSWORD_PATTERN = "^[a-zA-Z0-9_]{6,16}$"; // 密码,6-16位英文字母、数字或者下划线
//	// 昵称正则表达式
//	public final static String NICKNAME_PATTERN = "^[\u4E00-\u9FA5A-Za-z0-9_-]+$";
//
//	// 微博内容中的@正则表达式
//	public final static Pattern AT_PATTERN = Pattern
//			.compile("@[\\u4e00-\\u9fa5\\w\\-]+");
//
//	// url正则表达式
//	// public final static String URL =
//	// "(http[s]{0,1}|ftp)://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?";
//	public final static String URL = "((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
//
//	// 微博内容中的url正则表达式
//	public final static Pattern URL_PATTERN = Pattern.compile(URL);
	
	// 正则判断是否为邮箱
	public static boolean isEmail(String s) {
		String regString = "[A-Z0-9a-z._]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,5}";
		return  s.matches(regString);
	}

	// 正则判断是否为手机号
	public static boolean isMobilePhoneNum(String s) {
		String regString = "^[1][3-8]\\d{9}$";
		return s.matches(regString);
	}

	// 判断是否全为数字组成
	public static boolean isOnlyDigital(String s) {
		String regString = "\\d+";
		return s.matches(regString);
	}

	// 判断是否全由数字和字母组成
	public static boolean isDigitalAndLetter(String s) {
		String regString = "[0-9a-zA-Z]+";
		return s.matches(regString);
	}
}
