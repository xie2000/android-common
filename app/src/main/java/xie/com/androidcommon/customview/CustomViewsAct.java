package xie.com.androidcommon.customview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import xie.com.androidcommon.R;
import xie.com.androidcommon.base.BaseActivity;
import xie.com.androidcommon.viewUtil.ExpandTextView;

/**
 * 自定义的view
 * Created by xiechengfa on 2017/5/10 14:04
 */
public class CustomViewsAct extends BaseActivity {
    public static void startAcy(Activity activity) {
        Intent intent=new Intent(activity, CustomViewsAct.class);
        intent.putExtra(BaseActivity.KEY_TITLE,"自定义的view");
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
    }
}
