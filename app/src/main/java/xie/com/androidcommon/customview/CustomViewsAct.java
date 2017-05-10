package xie.com.androidcommon.customview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import xie.com.androidcommon.R;
import xie.com.androidcommon.viewUtil.ExpandTextView;

/**
 * 自定义的view
 * Created by xiechengfa on 2017/5/10 14:04
 */
public class CustomViewsAct extends AppCompatActivity {
    public static void startAcy(Activity activity) {
        activity.startActivity(new Intent(activity, CustomViewsAct.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("自定义的view");
        setContentView(R.layout.activity_customviews);

        ExpandTextView expandTextView1= (ExpandTextView) findViewById(R.id.tv_expand1);
        ExpandTextView expandTextView2= (ExpandTextView) findViewById(R.id.tv_expand2);
        expandTextView1.setText("每个人都会有一段异常艰难的时光，生活的窘迫，工作的失意，学业的压力，爱的惶惶不可终日。挺过来的，人生就会豁然开朗；挺不过来的，时间也会教会你怎么与它们握手言和，所以你都不必害怕的");
        expandTextView2.setText("每个人都会有一段异常艰难的时光，生活的窘迫，工作的失意，学业的压力，爱的惶惶不可终日。挺过来的，人生就会豁然开朗；挺不过来的，时间也会教会你怎么与它们握手言和，所以你都不必害怕的");
    }
}
