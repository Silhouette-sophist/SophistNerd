package com.example.squareup.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.MainThread

@MainThread
fun showToastMessage(context : Context, msg : String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}