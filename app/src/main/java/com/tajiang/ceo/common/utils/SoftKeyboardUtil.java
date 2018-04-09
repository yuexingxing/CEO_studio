package com.tajiang.ceo.common.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Method;

/**
 * Created by SQL on 2016/7/27.
 */
public class SoftKeyboardUtil {

    public static void observeSoftKeyboard(final Activity activity, final OnSoftKeyboardChangeListener listener) {
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int previousKeyboardHeight = -1;
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHeight = rect.bottom - rect.top;
                int height = decorView.getHeight()
                        - ScreenUtil.getStatuesBarHeight(activity)
                        - ScreenUtil.getNavigationBarHeight(activity);
                int keyboardHeight = height - displayHeight;
                if (previousKeyboardHeight != keyboardHeight) {
                    boolean hide = (double) displayHeight / height > 0.8;
                    listener.onSoftKeyBoardChange(keyboardHeight, height, !hide);
                }

                previousKeyboardHeight = height;

            }
        });
    }

    public interface OnSoftKeyboardChangeListener {
        void onSoftKeyBoardChange(int softKeybardHeight, int screenHeight, boolean visible);
    }

    public static void hideSoftKeyboard(Activity activity) {
        ((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken()
                        , InputMethodManager.HIDE_NOT_ALWAYS);
    }
}