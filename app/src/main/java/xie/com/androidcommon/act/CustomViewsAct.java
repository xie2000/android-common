package xie.com.androidcommon.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import xie.com.androidcommon.R;
import xie.com.androidcommon.base.BaseActivity;
import xie.com.androidcommon.customview.ExpandTextView;
import xie.com.androidcommon.customview.XStateButton;

/**
 * 自定义的view
 * Created by xiechengfa on 2017/5/10 14:04
 */
public class CustomViewsAct extends BaseActivity {
    public static void startAcy(Activity activity) {
        Intent intent = new Intent(activity, CustomViewsAct.class);
        intent.putExtra(BaseActivity.KEY_TITLE, "自定义的view");
        activity.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_customviews;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @Override
    public void initView() {
        ExpandTextView expandTextView1 = (ExpandTextView) findViewById(R.id.tv_expand1);
        expandTextView1.setText("每个人都会有一段异常艰难的时光，生活的窘迫，工作的失意，学业的压力，爱的惶惶不可终日。挺过来的，人生就会豁然开朗；挺不过来的，时间也会教会你怎么与它们握手言和，所以你都不必害怕的");

        //设置不同状态下文字变色
        final XStateButton text = (XStateButton) findViewById(R.id.text_test);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text.setEnabled(false);
            }
        });

        //最常用的设置不同背景
        final XStateButton background = (XStateButton) findViewById(R.id.background_test);
        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                background.setEnabled(false);
            }
        });

        //设置四个角不同的圆角
        final XStateButton radius = (XStateButton) findViewById(R.id.different_radius_test);
        radius.setRadius(new float[]{0, 0, 20, 20, 40, 40, 60, 60});


        //设置不同状态下边框颜色，宽度
        final XStateButton stroke = (XStateButton) findViewById(R.id.stroke_test);
        stroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stroke.setEnabled(false);
            }
        });

        //设置间断
        final XStateButton dash = (XStateButton) findViewById(R.id.dash_test);
        dash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dash.setEnabled(false);
            }
        });
    }
}
