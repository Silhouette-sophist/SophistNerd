package com.example.sophistnerd.util

import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.annotation.MainThread
import com.example.sophistnerd.SophistApplication
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

@MainThread
private fun showMessage(msg: String, showLong: Boolean = false) {
    Toast.makeText(
        SophistApplication.sContext,
        msg,
        if (showLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    )
        .also {
            it.setGravity(Gravity.CENTER, 0, 0)
            it.show()
        }
}

fun showMessageSafely(msg: String, showLong: Boolean = false) {
    if (!isMainThread()) {
        Handler(Looper.getMainLooper()).post {
            showMessage(msg, showLong)
        }
    } else {
        showMessage(msg, showLong)
    }
}

fun isMainThread() = Looper.getMainLooper() == Looper.myLooper()

@MainThread
fun showSnackbarMessage(anchorView: View, msg: String) {
    Snackbar.make(anchorView, msg, BaseTransientBottomBar.LENGTH_SHORT)
        .setAction("确定") {

        }
        .show()
}