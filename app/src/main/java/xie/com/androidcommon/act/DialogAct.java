package xie.com.androidcommon.act;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import xie.com.androidcommon.R;
import xie.com.androidcommon.base.BaseActivity;
import xie.com.androidcommon.dialog.CustomDialog;
import xie.com.androidcommon.dialog.CustomProgressDialog;
import xie.com.androidcommon.dialog.DialogUtils;
import xie.com.androidcommon.dialog.ListDialog;
import xie.com.androidcommon.dialog.ListDialogMenuInfo;
import xie.com.androidcommon.dialog.LoadingDialog;

/**
 * Created by xiechengfa on 2016/9/22.
 * 系统信息(分辨率、SDK版本、IMEI号等)
 */
public class DialogAct extends BaseActivity implements View.OnClickListener {
    public static void startAcy(Activity activity) {
        Intent intent = new Intent(activity, DialogAct.class);
        intent.putExtra(BaseActivity.KEY_TITLE, "各种对话框");
        activity.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_dialog;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @Override
    public void initView() {
        findViewById(R.id.loading1).setOnClickListener(this);
        findViewById(R.id.loading2).setOnClickListener(this);
        findViewById(R.id.loading3).setOnClickListener(this);
        findViewById(R.id.alert_dialog).setOnClickListener(this);
        findViewById(R.id.list_dialog).setOnClickListener(this);
        findViewById(R.id.toast).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loading1) {
            LoadingDialog.Build(this)
                    .setMessageColor(Color.BLACK)
                    .setOnCancelLoadingDiaListener(new LoadingDialog.OnCancelLoadingDiaListener() {
                        @Override
                        public void onCancelLoadingDia() {
                            //to do
                        }
                    })
                    .show();
        } else if (v.getId() == R.id.loading2) {
            LoadingDialog.Build(this)
                    .setOrientation(LinearLayout.HORIZONTAL)
                    .setBackgroundColor(Color.parseColor("#aa0000ff"))
                    .setMessageColor(Color.WHITE)
                    .setMessage("正在加载中...")
                    .setOnCancelLoadingDiaListener(new LoadingDialog.OnCancelLoadingDiaListener() {
                        @Override
                        public void onCancelLoadingDia() {
                            //to do
                        }
                    }).show();
        } else if (v.getId() == R.id.loading3) {
            CustomProgressDialog progressDialog = new CustomProgressDialog(this);
            progressDialog.setOnCancelProgressDiaListener(new CustomProgressDialog.OnCancelProgressDiaListener() {
                @Override
                public void onCancelProgressDia() {
                    //to do
                }
            });
            progressDialog.show();
        } else if (v.getId() == R.id.alert_dialog) {
            DialogUtils.showMessageDialog(this, "退出", "确定要退出应用？", "确定", "取消", new CustomDialog.CustomDialogListener() {
                @Override
                public void onConfirmBtnClicked() {
                }

                @Override
                public void onCancelBtnClicked() {
                }
            });
        }else if(v.getId()==R.id.list_dialog){
            ArrayList<ListDialogMenuInfo> sheetItemList = new ArrayList<>();
            sheetItemList.add(new ListDialogMenuInfo(1,"item1"));
            sheetItemList.add(new ListDialogMenuInfo(2,"item2"));
            sheetItemList.add(new ListDialogMenuInfo(3,"item3"));
            sheetItemList.add(new ListDialogMenuInfo(5,"item4"));

            DialogUtils.showListDialog(this, sheetItemList, new ListDialog.OnListDialogItemClickListener() {
                @Override
                public void onItemClicked(int code) {

                }
            });
        }else if(v.getId()==R.id.toast){
            DialogUtils.showToast("toast测试");
        }
    }
}
