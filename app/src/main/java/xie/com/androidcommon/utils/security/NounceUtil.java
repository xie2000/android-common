package xie.com.androidcommon.utils.security;

public class NounceUtil {

    private static final String NOUNCE_KEY = "d897a7d387a34e389e0b71013a6dbe4c";
    private static final int WEEK = 7 * 24 * 60 * 60;

    public static String getNounce(int timeStamp) {
        return timeStamp / WEEK + NOUNCE_KEY;
    }
}
