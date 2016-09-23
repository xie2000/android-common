package xie.com.androidcommon.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文本处理工具
 * Created by xiechengfa on 2016/9/23.
 */
public class CommonTextUtils {
    /**
     * 检测字符串中是否包含汉字
     * @param sequence
     * @return
     */
    public static boolean checkChinese(String sequence) {
        final String format = "[\\u4E00-\\u9FA5\\uF900-\\uFA2D]";
        boolean result = false;
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(sequence);
        result = matcher.find();
        return result;
    }

    /**
     * 检测字符串中只能包含：中文、数字、下划线(_)、横线(-)
     * @param sequence
     * @return
     */
    public  boolean checkNickname(String sequence) {
        final String format = "[^\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w-_]";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(sequence);
        return !matcher.find();
    }
}
