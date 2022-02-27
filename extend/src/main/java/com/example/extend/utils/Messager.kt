package com.example.testlifecycle.utils

import java.lang.IllegalStateException

fun showPositionLog() {

    val illegalStateException = IllegalStateException()
    val stackTraceElement = illegalStateException.stackTrace[1]
    val split = stackTraceElement.className.split(".")
    //println("SophistNerd $split")
    val className = split[split.size - 1]
    val methodName = stackTraceElement.methodName
    var positionMsg = "SophistNerd"
    println("$positionMsg $className $methodName")
}