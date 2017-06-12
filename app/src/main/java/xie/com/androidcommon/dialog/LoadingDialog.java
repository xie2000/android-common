package xie.com.androidcommon.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import xie.com.androidcommon.R;
import xie.com.androidcommon.utils.ResUtils;
import xie.com.androidcommon.utils.CustomColorDrawable;
import xie.com.androidcommon.utils.OutdatedUtils;

/**
 * 加载对话框
 */
public class LoadingDialog extends Dialog {
    private static LoadingDialog dialog;

    private Context context;
    private LinearLayout loadingView;
    private ProgressBar progressBar;
    private TextView loadingMessage;
    private CustomColorDrawable drawable;
    private OnCancelLoadingDiaListener listener;

    public LoadingDialog(Context context) {
        super(context, R.style.loading_dialog);
        this.context = context;
        setContentView(R.layout.dialog_loading);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        loadingMessage = (TextView) findViewById(R.id.loading_message);
        progressBar = (ProgressBar) findViewById(R.id.loading_progressbar);
        loadingView = (LinearLayout) findViewById(R.id.ll_loading);
        loadingMessage.setPadding(0, ResUtils.getResDimensionPixelSize(R.dimen.spacing), 0, 0);

        drawable = new CustomColorDrawable();
        drawable.setColor(Color.WHITE);
        OutdatedUtils.setBackground(loadingView, drawable);
    }

    public static LoadingDialog Build(Context context) {
        if (dialog == null) {
            dialog = new LoadingDialog(context);
        }
        return dialog;
    }

    public LoadingDialog setOrientation(int orientation) {
        loadingView.setOrientation(orientation);
        if (orientation == LinearLayout.HORIZONTAL) {
            loadingMessage.setPadding(ResUtils.getResDimensionPixelSize(R.dimen.spacing), 0, 0, 0);
        } else {
            loadingMessage.setPadding(0, ResUtils.getResDimensionPixelSize(R.dimen.spacing), 0, 0);
        }
        return dialog;
    }

    public LoadingDialog setBackgroundColor(@ColorInt int color) {
        drawable.setColor(color);
        OutdatedUtils.setBackground(loadingView, drawable);
        return dialog;
    }

    public LoadingDialog setMessage(String message) {
        if (!TextUtils.isEmpty(message)) {
            loadingMessage.setText(message);
        }
        return this;
    }

    public LoadingDialog setMessageColor(@ColorInt int color) {
        loadingMessage.setTextColor(color);
        return this;
    }

    public LoadingDialog setOnCancelLoadingDiaListener(OnCancelLoadingDiaListener listener) {
        this.listener = listener;
        return this;
    }


    @Override
    public void dismiss() {
        super.dismiss();
        if (dialog != null)
            dialog = null;
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(listener!=null) {
                listener.onCancelLoadingDia();
                dismiss();
            }
        }
        return true;
    }

    /**
     * 关闭加载对话框回调
     */
    public interface OnCancelLoadingDiaListener {
        /**
         * 关闭对话框
         */
        public void onCancelLoadingDia();
    }
}
