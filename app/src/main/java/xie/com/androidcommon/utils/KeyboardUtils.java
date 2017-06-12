package xie.com.androidcommon.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 键盘相关工具类
 */
public class KeyboardUtils {

    private KeyboardUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 关闭activity中打开的键盘
     *
     * @param activity
     */
    public static void closeKeyboard(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 关闭dialog中打开的键盘
     *
     * @param dialog
     */
    public static void closeKeyboard(Dialog dialog) {
        View view = dialog.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) dialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 打开键盘
     *
     * @param context
     * @param editText
     */
    public static void openKeyboard(final Context context, final EditText editText) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                editText.setSelection(editText.getText().toString().length());
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            }
        }, 300);
    }

    /**
     * 拷贝文档到黏贴板
     *
     * @param text
     */
    public static void clip(Context context, String text) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setText(text);
        } else {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(ClipData.newPlainText("content", text));
        }
    }

    /**
     * 切换键盘的显示与隐藏
     *
     * @param activity
     */
    public static void toggleKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 处理点击非 EditText 区域时，自动关闭键盘
     *
     * @param isAutoCloseKeyboard 是否自动关闭键盘
     * @param currentFocusView    当前获取焦点的控件
     * @param motionEvent         触摸事件
     * @param dialogOrActivity    Dialog 或 Activity
     */
    public static void handleAutoCloseKeyboard(boolean isAutoCloseKeyboard, View currentFocusView, MotionEvent motionEvent, Object dialogOrActivity) {
        if (isAutoCloseKeyboard && motionEvent.getAction() == MotionEvent.ACTION_DOWN && currentFocusView != null && (currentFocusView instanceof EditText) && dialogOrActivity != null) {
            int[] leftTop = {0, 0};
            currentFocusView.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + currentFocusView.getHeight();
            int right = left + currentFocusView.getWidth();
            if (!(motionEvent.getX() > left && motionEvent.getX() < right && motionEvent.getY() > top && motionEvent.getY() < bottom)) {
                if (dialogOrActivity instanceof Dialog) {
                    KeyboardUtils.closeKeyboard((Dialog) dialogOrActivity);
                } else if (dialogOrActivity instanceof Activity) {
                    KeyboardUtils.closeKeyboard((Activity) dialogOrActivity);
                }
            }
        }
    }

    /**
     * 设置光标
     *
     * @param editText
     */
    public static void setEditTextFocus(EditText editText) {
        if (editText == null) {
            return;
        }

        editText.requestFocus();
        editText.setSelection(editText.getEditableText().toString().length());
    }

    /**
     * @Desp:设置默认输入法
     * @param @param editText
     * @param @param type<br/>
     *        InputType.TYPE_CLASS_TEXT-中文<br/>
     *        InputType.TYPE_TEXT_VARIATION_URI-英文<br/>
     *        InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS-email</br/>
     *        InputType.TYPE_TEXT_VARIATION_PASSWORD-密码<br/>
     *        InputType.TYPE_CLASS_PHONE－手机号<br/>
     *        InputType.TYPE_CLASS_NUMBER－数字<br/>
     * @return void
     */
    public static void setInputType(EditText editText, int type) {
        if (editText == null) {
            return;
        }

        editText.setInputType(type);
    }

    /**
     * @Desp: 设置输入法中回车按钮的显示内容
     * @param @param editText
     * @param @param action<br/>
     *        EditorInfo.IME_ACTION_SEARCH 搜索 <br/>
     *        EditorInfo.IME_ACTION_SEND 发送<br/>
     *        EditorInfo.IME_ACTION_NEXT 下一步<br/>
     *        EditorInfo.IME_ACTION_DONE 完成<br/>
     *        EditorInfo.IME_ACTION_NEXT 去往<br/>
     * @return void
     */
    public static void setImeOptions(EditText editText, int action) {
        if (editText == null) {
            return;
        }

        editText.setImeOptions(action);
    }

    /**
     * @Desp:设置监听输入法中的回车按钮
     * @param @param editText
     * @param @param listener
     * @return void
     */
    public static void setOnKeyListener(EditText editText, final MyInputMethodOnKeyListener listener) {
        if (editText == null) {
            return;
        }

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    if (listener != null) {
                        listener.onInputMethodeOnkey();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * @Desp:禁用EditText的复制、粘贴功能
     * @param @param editText
     * @return void
     */
    public static void fobiddenSelectionAction(EditText editText) {
        if (editText == null) {
            return;
        }

        editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        editText.setLongClickable(false);
    }

    // 回车的监听事件
    public interface MyInputMethodOnKeyListener {
        // 回车的事件
        public void onInputMethodeOnkey();
    }
}
