package com.avkd.humible.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    static public void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
