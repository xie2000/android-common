package xie.com.androidcommon.util;

import android.app.Activity;
import android.content.Context;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 输入法工具类
 * 
 * @Desp:
 * @author xiechengfa2000@163.com
 * @date 2015-6-23 上午9:40:41
 */
public class InputMethodUtils {
    /**
     * @Desp: 切换输入法
     * @return void
     */
    public static void switchSoftKeyBoard(Activity act) {
        if (act == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        boolean bRet = imm.isActive();
        if (bRet) {
            // 切换开启和关闭
            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 打开输入法
     */
    public static void openInputMethod(Activity activity) {
        if (activity == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * @Desp:关闭输入法
     * @param @param context
     * @return void
     */
    public static boolean closeSoftKeyBoard(Activity activity) {
        if (activity == null) {
            return false;
        }

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        boolean bRet = imm.isActive();
        if (bRet) {
            // 如果存在关闭
            if (activity != null && activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            } else {
                return false;
            }
            // 切换开启和关闭
            // imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
            // InputMethodManager.HIDE_NOT_ALWAYS);
            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }

        return false;
    }

    public static void closeKeyBoardOfView(Activity activity, View view) {
        if (activity == null) {
            return;
        }

        if (view == null) {
            return;
        }

        if (view.getWindowToken() == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // /**
    // * @Desp:关闭输入法
    // * @param @param context
    // * @return void
    // */
    // public static boolean closeSoftKeyBoard(Activity activity) {
    // if (activity == null) {
    // return false;
    // }
    //
    // InputMethodManager imm = (InputMethodManager)
    // activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    // // 得到InputMethodManager的实例
    // boolean bRet = imm.isActive();
    // if (bRet) {
    // // 如果存在关闭
    // if (activity.getCurrentFocus() != null) {
    // imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
    // InputMethodManager.HIDE_NOT_ALWAYS);
    // return true;
    // } else {
    // return false;
    // }
    // // 切换开启和关闭
    // // imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
    // // InputMethodManager.HIDE_NOT_ALWAYS);
    // // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
    // }
    //
    // return false;
    // }

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

        editText.setOnKeyListener(new OnKeyListener() {
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

    // /**
    // * @Desp: 限制输入内容
    // * @param @param editText
    // * @return void
    // */
    // public static void setInputMethodFileter(Activity activity,
    // EditText editText) {
    // InputTxtFilter.inputFilter(activity, editText,
    // InputTxtFilter.INPUT_TYPE_EN, 5);
    // }

    // 回车的监听事件
    public interface MyInputMethodOnKeyListener {
        // 回车的事件
        public void onInputMethodeOnkey();
    }

    // // 限制输入内容的过滤器
    // public static class InputTxtFilter {
    // public static final int INPUT_TYPE_EN = 0x01;
    // public static final int INPUT_TYPE_CH = 0x02;
    // private static final String[] SPELL = new String[] { "a", "b", "c",
    // "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
    // "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "ā",
    // "á", "ǎ", "à", "ō", "ó", "ǒ", "ò", "ē", "é", "ě", "è", "ī",
    // "í", "ǐ", "ì", "ū", "ú", "ǔ", "ù", "ǖ", "ǘ", "ǚ", "ǜ", "ü" };
    // private static char[] chineseParam = new char[] { '」', '，', '。', '？',
    // '…', '：', '～', '【', '＃', '、', '％', '＊', '＆', '＄', '（', '‘',
    // '’', '“', '”', '『', '〔', '｛', '【', '￥', '￡', '‖', '〖', '《',
    // '「', '》', '〗', '】', '｝', '〕', '』', '”', '）', '！', '；', '—' };
    //
    // public static void inputFilter(final Context context,
    // final EditText editText, final int type, final int inputLimit) {
    // InputFilter[] filters = new InputFilter[1];
    // filters[0] = new InputFilter.LengthFilter(inputLimit) {
    // public CharSequence filter(CharSequence source, int start,
    // int end, Spanned dest, int dstart, int dend) {
    // boolean isRightCharater = false;
    // if (type == INPUT_TYPE_EN) {
    // isRightCharater = isLetter(source.toString());
    // } else if (type == INPUT_TYPE_CH) {
    // isRightCharater = isChineseWord(source.toString());
    // }
    //
    // if (!isRightCharater
    // || dest.toString().length() >= inputLimit) {
    // return "";
    // }
    //
    // return source;
    // }
    // };
    // editText.setFilters(filters);
    // }
    //
    // /**
    // * 检测String是否全是中文
    // *
    // */
    // public static boolean isChineseWord(String name) {
    // boolean res = true;
    // char[] cTemp = name.toCharArray();
    //
    // for (int i = 0; i < name.length(); i++) {
    // if (!isChinese(cTemp[i])) {
    // res = false;
    // break;
    // }
    // }
    //
    // return res;
    // }
    //
    // /**
    // * 是否为英文字母
    // *
    // * */
    // public static boolean isLetter(String inputStr) {
    // char[] inputArray = inputStr.toCharArray();
    // List<String> spellList = Arrays.asList(SPELL);
    //
    // for (char input : inputArray) {
    // if (!spellList.contains(input + "")) {
    // return false;
    // }
    // }
    //
    // return true;
    // }
    //
    // /**
    // * 判定输入汉字
    // *
    // * @param c
    // */
    // public static boolean isChinese(char c) {
    // for (char param : chineseParam) {
    // if (param == c) {
    // return false;
    // }
    // }
    //
    // Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
    // if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
    // || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
    // || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
    // || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
    // || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
    // || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
    // return true;
    // }
    //
    // return false;
    // }
    // }
}
