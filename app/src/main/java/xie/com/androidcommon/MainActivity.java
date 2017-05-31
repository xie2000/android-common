package xie.com.androidcommon;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import xie.com.androidcommon.base.BaseActivity;
import xie.com.androidcommon.customview.CustomViewsAct;
import xie.com.androidcommon.systeminfo.SystemInfoAct;
import xie.com.androidcommon.utils.XPreferencesUtils;
import xie.com.androidcommon.utils.XPrintUtils;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    private final String ITEM_SYSTEMINFO = "系统信息(分辨率、SDK版本、IMEI号等)";
    private final String ITEM_CUSTOMVIEWS = "自定义的View";
    private ArrayList<String> list = null;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        list = new ArrayList<>();
        list.add(ITEM_SYSTEMINFO);
        list.add(ITEM_CUSTOMVIEWS);

        XPreferencesUtils.put("xie",1234);

        XPrintUtils.d("*********************xie get:"+XPreferencesUtils.get("xie",0));
    }

    @Override
    public void initView() {
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_activated_1,android.R.id.text1,list);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
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
