package com.example.sophistnerd.util

import android.os.Handler
import android.os.Looper
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
    ).show()
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

    if (anchorView.isAttachedToWindow) {
        Snackbar.make(anchorView, msg, BaseTransientBottomBar.LENGTH_SHORT)
            .setAction("确定") {

            }
            .show()
    }
}

//增加偏函数支持！！！方便传递函数参数。1.偏函数实现，2.匿名函数实现，3.lambda表达式实现
fun <P1, P2, R> Function2<P1, P2, R>.partial1(p1: P1) = fun(p2: P2) = this(p1, p2)
fun <P1, P2, R> Function2<P1, P2, R>.partial2(p2: P2) = fun(p1: P1) = this(p1, p2)
