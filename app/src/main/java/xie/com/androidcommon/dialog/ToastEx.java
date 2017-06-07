package xie.com.androidcommon.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import xie.com.androidcommon.R;

public class ToastEx extends Toast {

    public ToastEx(Context context) {
        super(context);
    }

    /**
     * Make a standard toast that just contains a text view.
     *
     * @param context The context to use. Usually your {@link android.app.Application} or
     *        {@link android.app.Activity} object.
     * @param text The text to show. Can be formatted text.
     * @param duration How long to display the message. Either {@link #LENGTH_SHORT} or
     *        {@link #LENGTH_LONG}
     *
     */
    public static ToastEx makeText(Context context, CharSequence text, int duration) {
        ToastEx result = new ToastEx(context);

        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.toast_layout, null);
        TextView tv = (TextView) v.findViewById(R.id.tv_message);
        tv.setText(text);

        result.setDuration(duration);
        result.setView(v);

        return result;
    }


    @Override
    public void setText(CharSequence s) {
        if (getView() == null) {
            return;
        }

        TextView tv = (TextView) getView().findViewById(R.id.tv_message);
        if (tv == null) {
            return;
        }

        tv.setText(s);
    }
}
