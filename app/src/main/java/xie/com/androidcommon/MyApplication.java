package xie.com.androidcommon;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

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
        instance = this;
    }
}
