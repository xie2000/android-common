package xie.com.androidcommon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import xie.com.androidcommon.util.ResUtil;
import xie.com.androidcommon.util.Utils;

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
        sb.append("分辨率："+Utils.getScreenWidth(this)+"X"+ Utils.getScreenHeight(this)+"\n");
        sb.append("状态栏高度："+Utils.getStatusBar());

        infoView.setText(sb.toString());
    }
}
