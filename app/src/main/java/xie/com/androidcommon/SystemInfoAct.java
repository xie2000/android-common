package xie.com.androidcommon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by xiechengfa on 2016/9/22.
 * 系统信息(分辨率、SDK版本、IMEI号等)
 */
public class SystemInfoAct extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private final String ITEM_SYSTEMINFO = "系统信息(分辨率、SDK版本、IMEI号等)";
    private ArrayList<String> list = null;

    public static void startAcy(Activity activity){
        activity.startActivity(new Intent(activity,SystemInfoAct.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listview);
        initData();

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_activated_1,android.R.id.text1,list);
        listView.setAdapter(adapter);
    }

    private void initData(){
        list = new ArrayList<>();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(list.get(i).equals(ITEM_SYSTEMINFO)){
            //系统信息
        }
    }
}
