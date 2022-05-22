package com.example.squareup.util

import java.util.*
import java.util.logging.Logger

object SquareUpLog {

    val logger = Logger.getLogger("SquareUpLog")

    fun info(msg : String) {
        val loggerTag = getLoggerTag()
        logger.info("[$loggerTag] $msg")
    }

    fun error(throwable: Throwable) {
        logger.warning("${throwable.message}, ${throwable.cause}, ${Arrays.toString(throwable.stackTrace)}")
    }

    private fun getLoggerTag() : String {
        val stackTrace = Thread.currentThread().stackTrace
        val stackTraceElement = stackTrace[stackTrace.size - 2]
        val methodName = stackTraceElement.methodName
        val simpleClassName = getSimpleClassName(stackTraceElement.className.split("$")[0])
        return "[$simpleClassName $methodName]"
    }

    private fun getSimpleClassName(className: String) : String{
        var simpleClassName = ""
        val split = className.split("$")
        if (split.isNotEmpty()) {
            val classSplit = split[0].split(".")
            if(classSplit.isNotEmpty()) {
                simpleClassName = classSplit[classSplit.size - 1]
            }
        }
        return simpleClassName
    }
}