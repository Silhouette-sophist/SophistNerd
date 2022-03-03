package com.example.sophistnerd.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.example.testlifecycle.utils.getMotionEventDesc
import com.example.testlifecycle.utils.showPositionLog

class SwipeImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {


    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        showPositionLog(getMotionEventDesc(event))
        return false
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        showPositionLog(getMotionEventDesc(event))
        return super.onTouchEvent(event)
    }
}