package com.example.squareup.util

import java.util.*
import java.util.logging.Logger

object SquareUpLog {

    val logger = Logger.getLogger("SquareUpLog")

    fun info(msg : String) {
        logger.info(msg)
    }

    fun error(throwable: Throwable) {
        logger.warning("${throwable.message}, ${throwable.cause}, ${Arrays.toString(throwable.stackTrace)}")
    }
}