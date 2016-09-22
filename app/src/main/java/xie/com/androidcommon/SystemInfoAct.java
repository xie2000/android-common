package xie.com.androidcommon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import xie.com.androidcommon.util.DeviceInfoUtil;

/**
 * Created by xiechengfa on 2016/9/22.
 * 系统信息(分辨率、SDK版本、IMEI号等)
 */
public class SystemInfoAct extends AppCompatActivity {

    public static void startAcy(Activity activity){
        activity.startActivity(new Intent(activity,SystemInfoAct.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systeminfo);

        TextView infoView = (TextView) findViewById(R.id.infoView);
        StringBuffer sb = new StringBuffer();
        sb.append("分辨率："+ DeviceInfoUtil.getScreenWidth(this)+"X"+ DeviceInfoUtil.getScreenHeight(this)+"\n");
        sb.append("状态栏高度："+DeviceInfoUtil.getStatusBar()+"\n");
        sb.append("手机型号："+DeviceInfoUtil.getHandsetType()+",SDK版本："+DeviceInfoUtil.getPhoneSDKVersionChar()+"("+DeviceInfoUtil.getPhoneSdkVersion()+"),imei:"+DeviceInfoUtil.getIMEI()+"\n");

        infoView.setText(sb.toString());
    }
}
