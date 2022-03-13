package com.example.extend.test

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


@OptIn(ExperimentalTime::class, kotlinx.coroutines.InternalCoroutinesApi::class)
fun main() {
    val block = {
        runBlocking {
            // 收集这个流
            simple().collect(object : FlowCollector<Int> {
                override suspend fun emit(value: Int) {
                    println("flow $value ${Thread.currentThread().name}")
                }

            })
        }
    }
    print("measureTime(block) = ${measureTime(block)}")

}

fun simple(): Flow<Int> = flow { // 流构建器
    for (i in 1..3) {
        delay(100) // 假装我们在这里做了一些有用的事情
        emit(i) // 发送下一个值
    }
}.flowOn(Dispatchers.IO)

