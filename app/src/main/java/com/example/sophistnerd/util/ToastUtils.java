package com.example.sophistnerd.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtils {

    public static boolean showToast(Context context, String msg) {

        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            toast.show();
        } else {
            new Handler(Looper.getMainLooper()).post(toast::show);
        }
        return true;
    }
}
