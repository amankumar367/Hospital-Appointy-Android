package com.example.aman.hospitalappointy.utils;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View currentFocusedView = activity.getCurrentFocus();
            if (currentFocusedView != null) {
                inputMethodManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), 0);
            }
        } catch (Exception e) {
            Log.e("hideKeyboard", "hideKeyboard: Couldn't hide keyboard");
        }
    }

}
