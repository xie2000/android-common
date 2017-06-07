package xie.com.androidcommon.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import xie.com.androidcommon.R;
import xie.com.androidcommon.utils.ResUtils;

/**
 * 列表PopupWindown
 *
 * @author xiechengfa
 * @date 2017/1/16
 */
public class ListPopupWindow implements AdapterView.OnItemClickListener {
    private boolean isAlignBottom = true;//是否显示在下方
    private int currIndex = -1;
    private PopupWindow pw;
    private Context context;
    private String[] itemArr;
    private View parentView;
    private AdapterView.OnItemClickListener listener;

    public ListPopupWindow(Context context) {
        this.context = context;
    }

    /**
     * 设置数据和相对位置的View
     *
     * @param itemArr
     * @param parentView
     */
    public void setData(String[] itemArr, View parentView, boolean isAlignBottom) {
        this.itemArr = itemArr;
        this.parentView = parentView;
        this.isAlignBottom = isAlignBottom;
        init();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void show(int currIndex) {
        this.currIndex = currIndex;
        show();
    }

    public void show() {
        if (pw.isShowing()) {
            return;
        }

        int[] location = new int[2];
        parentView.getLocationOnScreen(location);
        pw.getContentView().measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        if (isAlignBottom) {
            pw.showAtLocation(parentView, Gravity.NO_GRAVITY, location[0] - ResUtils.dip2px(1), location[1] + parentView.getMeasuredHeight());
        } else {
            //pw.getContentView().getMeasuredHeight()只有一个item的高度，所以要乘以Size
            pw.showAtLocation(parentView, Gravity.NO_GRAVITY, location[0] - ResUtils.dip2px(1), location[1] - itemArr.length*pw.getContentView().getMeasuredHeight() - ResUtils.dip2px(15));
        }
    }

    public void dismiss() {
        pw.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismiss();
        if (listener != null) {
            listener.onItemClick(parent, view, position, id);
        }
    }

    private void init() {
        ListView listView = new ListView(context);
        ColorDrawable dividerDrawable = new ColorDrawable();
        dividerDrawable.setColor(0xff737373);
        listView.setDivider(dividerDrawable);
        listView.setDividerHeight(1);
        listView.setVerticalScrollBarEnabled(false);
        listView.setCacheColorHint(0x00000000);
        listView.setOnItemClickListener(this);
        ColorDrawable selectorDrawable = new ColorDrawable();
        selectorDrawable.setColor(0x00000000);
        listView.setSelector(selectorDrawable);
        listView.setAdapter(new TeacherListAdapter(itemArr));

        pw = new PopupWindow(context);
        pw.setWidth(parentView.getMeasuredWidth());
        pw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setTouchable(true);
        pw.setOutsideTouchable(true);
        pw.setFocusable(true);
        pw.setBackgroundDrawable(ResUtils.getResDrawable(isAlignBottom ? R.drawable.img_quote_pw_bg : R.drawable.img_quote_pw_down_bg));

        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        pw.setContentView(listView);
    }

    private class TeacherListAdapter extends BaseAdapter {
        private String[] itemArr;

        public TeacherListAdapter(String[] itemArr) {
            this.itemArr = itemArr;
        }

        @Override
        public int getCount() {
            if (itemArr == null) {
                return 0;
            }
            return itemArr.length;
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_list_popupwindow, parent, false);
                vh.nameView = (TextView) convertView.findViewById(R.id.item_tv_title);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            if (currIndex == position) {
                vh.nameView.setTextColor(ResUtils.getResColor(R.color.c1));
            } else {
                vh.nameView.setTextColor(ResUtils.getResColor(R.color.t4));
            }
            vh.nameView.setText(itemArr[position]);
            return convertView;
        }

        private class ViewHolder {
            public TextView nameView;
        }
    }
}
