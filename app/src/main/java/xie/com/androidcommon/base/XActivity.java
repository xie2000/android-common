package xie.com.androidcommon.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import xie.com.androidcommon.utils.ActivityStackManager;
import xie.com.androidcommon.utils.PermissionUtils;

public abstract class XActivity extends AppCompatActivity implements ICallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActivityStackManager.getInstance().addActivity(this);
        if (getLayoutId()>0) {
            setContentView(getLayoutId());
        }
        initData(savedInstanceState);
        initView();
        super.onCreate(savedInstanceState);

    }

    /**
     * Android M 全局权限申请回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStackManager.getInstance().finishActivity();
    }
}
