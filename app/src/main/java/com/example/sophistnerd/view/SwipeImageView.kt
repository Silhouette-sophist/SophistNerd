package com.example.sophistnerd.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

class SwipeImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {

    companion object {
        const val THRESHOLD = 200
    }
    //手指按下的点为(pointDownX, pointDownY)手指离开屏幕的点为(pointUpX, pointUpY)
    private var pointDownX = 0f
    private var pointDownY = 0f
    private var pointUpX = 0f
    private var pointUpY = 0f

    //图片上滑动的回调逻辑
    var swipeCallback : SwipeCallback? = null

    /**
     * 注意：
     * - swipeCallback被设置后就会回调，处理、消耗事件
     * - swipeCallback未被设置，则走默认事件处理流程
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        swipeCallback?.let {
            //继承了Activity的onTouchEvent方法，直接监听点击事件
            if (event?.action == MotionEvent.ACTION_DOWN) {
                //当手指按下的时候
                pointDownX = event.x
                pointDownY = event.y
            } else if (event?.action == MotionEvent.ACTION_UP) {
                //当手指离开的时候
                pointUpX = event.x
                pointUpY = event.y
                when {
                    pointDownY - pointUpY > THRESHOLD -> {
                        it.swipeUp()
                    }
                    pointUpY - pointDownY > THRESHOLD -> {
                        it.swipeDown()
                    }
                    pointDownX - pointUpX > THRESHOLD -> {
                        it.swipeNext()
                    }
                    pointUpX - pointDownX > THRESHOLD -> {
                        it.swipePrevious()
                    }
                }
            }
        }
        //注意，是扩展ImageView逻辑，非完全自定义View，所以在附加逻辑之外，还是走ImageView的逻辑！
        return super.onTouchEvent(event)
    }


    /**
     * 处理图片上的滑动操作逻辑
     */
    interface SwipeCallback {
        fun swipeUp()
        fun swipeDown()
        fun swipePrevious()
        fun swipeNext()
    }
}