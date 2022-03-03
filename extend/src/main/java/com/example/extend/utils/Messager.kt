package com.example.testlifecycle.utils

import android.view.MotionEvent
import java.lang.IllegalStateException

fun showPositionLog(msg : String = "") {

    val illegalStateException = IllegalStateException()
    val stackTraceElement = illegalStateException.stackTrace[1]
    val split = stackTraceElement.className.split(".")
    //println("SophistNerd $split")
    val className = split[split.size - 1]
    val methodName = stackTraceElement.methodName
    var positionMsg = "SophistNerd"
    println("$positionMsg $className $methodName $msg")
}

fun getMotionEventDesc(motionEvent: MotionEvent?) : String{
    return when(motionEvent?.action){
        MotionEvent.ACTION_DOWN -> "ACTION_DOWN"
        MotionEvent.ACTION_MOVE -> "ACTION_MOVE"
        MotionEvent.ACTION_UP -> "ACTION_UP"
        else -> {"${motionEvent?.action}"}
    }
}