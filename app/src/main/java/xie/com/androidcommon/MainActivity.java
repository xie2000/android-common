package xie.com.androidcommon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import xie.com.androidcommon.act.CustomViewsAct;
import xie.com.androidcommon.act.DialogAct;
import xie.com.androidcommon.act.SystemInfoAct;
import xie.com.androidcommon.base.BaseActivity;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private final String ITEM_SYSTEMINFO = "系统信息(分辨率、SDK版本、IMEI号等)";
    private final String ITEM_CUSTOMVIEWS = "自定义的View";
    private final String ITEM_DIALOG = "各种对话框";
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
        list.add(ITEM_DIALOG);
    }

    @Override
    public void initView() {
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        listView.setAdapter(new ListAdapter(list));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (list.get(i).equals(ITEM_SYSTEMINFO)) {
            //系统信息
            SystemInfoAct.startAcy(this);
        } else if (list.get(i).equals(ITEM_CUSTOMVIEWS)) {
            //自定义View
            CustomViewsAct.startAcy(this);
        } else if (list.get(i).equals(ITEM_DIALOG)) {
            //各种对话框
            DialogAct.startAcy(this);
        }
    }


    private class ListAdapter extends BaseAdapter {
        private ArrayList<String> list;

        public ListAdapter(ArrayList<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = LayoutInflater.from(MyApplication.getInstance()).inflate(R.layout.main_listview_item, parent, false);
                vh.nameView = (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            vh.nameView.setText(list.get(position));

            return convertView;
        }
    }

    private class ViewHolder {
        public TextView nameView;
    }
}
