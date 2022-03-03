package com.example.sophistnerd.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import com.example.testlifecycle.utils.getMotionEventDesc
import com.example.testlifecycle.utils.showPositionLog

class CustomLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        showPositionLog(getMotionEventDesc(ev))
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        showPositionLog(getMotionEventDesc(ev))
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        showPositionLog(getMotionEventDesc(event))
        return super.onTouchEvent(event)
    }
}