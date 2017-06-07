package xie.com.androidcommon.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import xie.com.androidcommon.R;
import xie.com.androidcommon.base.BaseActivity;
import xie.com.androidcommon.utils.ResUtils;
import xie.com.androidcommon.utils.DeviceUtils;

/**
 * Created by xiechengfa on 2016/9/22.
 * 系统信息(分辨率、SDK版本、IMEI号等)
 */
public class SystemInfoAct extends BaseActivity {
    public static void startAcy(Activity activity) {
        Intent intent=new Intent(activity, SystemInfoAct.class);
        intent.putExtra(BaseActivity.KEY_TITLE,"系统信息");
        activity.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_systeminfo;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @Override
    public void initView() {
        TextView infoView = (TextView) findViewById(R.id.infoView);
        StringBuffer sb = new StringBuffer();
        sb.append("分辨率：" + ResUtils.getScreenWidth() + "X" + ResUtils.getScreenHeight() + "\n");
        sb.append("状态栏高度：" + ResUtils.getStatusBarHeight() + "\n");
        sb.append("手机型号：" + DeviceUtils.getHandsetType() + "\n");
        sb.append("SDK版本：" + DeviceUtils.getPhoneSDKVersionName() + "(" + DeviceUtils.getPhoneSdkVersionCode() + ")\n");
        sb.append("IMEI:" + DeviceUtils.getIMEI() + "\n");
        sb.append("设备指纹:"+DeviceUtils.createFingerprint()+"\n");

        infoView.setText(sb.toString());
    }
}
