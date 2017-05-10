package xie.com.androidcommon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import xie.com.androidcommon.customview.CustomViewsAct;
import xie.com.androidcommon.systeminfo.SystemInfoAct;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private final String ITEM_SYSTEMINFO = "系统信息(分辨率、SDK版本、IMEI号等)";
    private final String ITEM_CUSTOMVIEWS = "自定义的View";
    private ArrayList<String> list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_activated_1,android.R.id.text1,list);
        listView.setAdapter(adapter);

        // UMeng 设置
        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.onPageStart(getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1));
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1));
    }

    private void initData(){
        list = new ArrayList<>();
        list.add(ITEM_SYSTEMINFO);
        list.add(ITEM_CUSTOMVIEWS);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(list.get(i).equals(ITEM_SYSTEMINFO)){
            //系统信息
            SystemInfoAct.startAcy(this);
        }else if(list.get(i).equals(ITEM_CUSTOMVIEWS)){
            //自定义View
            CustomViewsAct.startAcy(this);
        }
    }
}
