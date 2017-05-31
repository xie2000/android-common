package xie.com.androidcommon;

import android.app.Application;
import android.util.DisplayMetrics;

import xie.com.androidcommon.utils.XCrashHandlerUtils;

/**
 * @author xiechengfa
 * @ClassName: ACGApplication
 * @Description:全局的Application
 * @date 2015年11月9日 下午6:19:43
 */
public class MyApplication extends Application {

    private static MyApplication instance = null;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        XCrashHandlerUtils.getInstance().init(this);
        instance = this;
    }

    public static DisplayMetrics getDisplayMetrics() {
        return getInstance().getResources().getDisplayMetrics();
    }
}
