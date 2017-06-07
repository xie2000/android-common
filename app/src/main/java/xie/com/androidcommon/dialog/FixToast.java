package xie.com.androidcommon.dialog;

import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import xie.com.androidcommon.MyApplication;

public class FixToast {
    public static final int TIME_SHORT = 0;
    public static final int TIME_LONG = 1;

    private static ToastEx mToast;

    public static void createMsg(String msg) {
        //非主线程调用忽略,防止线程错误
        if (Looper.myLooper() != Looper.getMainLooper())
            return;

        if (TextUtils.isEmpty(msg)) {
            return;
        }

        if (mToast == null) {
            mToast = ToastEx.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT);
        } else {
            // View mNextView = mToast.getView();
            // if (mNextView == null || mNextView.findViewById(R.id.tv_message) == null) {
            // mToast.cancel();
            // mToast = null;
            // return;
            // }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                mToast.cancel();
            }
        }

        mToast.setText(msg);
        mToast.show();
    }
}
