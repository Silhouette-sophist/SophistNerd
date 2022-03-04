package com.example.extend.test

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {

    val channel = Channel<Int>(3)

    runBlocking {
        val producer = launch {
            var i = 0
            while (true) {
                channel.send(i++)
                delay(1000)
            }
        }

        val consumer = launch {
            val iterator = channel.iterator()

            while (iterator.hasNext()) { // 挂起点
                val element = iterator.next()
                println("receive iterator $element")
                delay(200)
            }
        }

        producer.join()
        consumer.join()
    }
}