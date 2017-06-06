package xie.com.androidcommon.act;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import xie.com.androidcommon.R;
import xie.com.androidcommon.base.BaseActivity;
import xie.com.androidcommon.dialog.CustomProgressDialog;
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
        findViewById(R.id.loading4).setOnClickListener(this);
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
        } else if (v.getId() == R.id.loading4) {
        }
    }
}
