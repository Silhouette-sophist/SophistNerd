package com.example.coroutine.test

import kotlinx.coroutines.*


fun main() {
    runBlocking {
        val base = Base()

        //1.普通高阶函数调用
        base.firstFunc("without wrapper...", Base::secondFunc)

        //2.最后一个函数类型参数，移出函数参数，并转化为lambda表达式调用形式
        base.firstFunc("with wrapper...") {
            it.secondFunc()
        }

        //注意，虽然二者赋值可相互使用，但是类型还是不同，具体在调用时。
        var simple = Base::firstFunc
        simple = Base::firstFuncSecond
    }
}

//以普通挂起函数定义
suspend fun Base.firstFunc(firstParam : String, secondFun : suspend (Base) -> Unit) {
    println("firstFunc called with $firstParam")
    secondFun(this)
}

//以扩展挂起函数定义
suspend fun Base.firstFuncSecond(firstParam : String, secondFun : suspend Base.() -> Unit) {
    println("firstFunc called with $firstParam")
    secondFun(this)
}

suspend fun Base.secondFunc() {
    delay(100)
    println("secondFunc called ...")
    "".also {

    }
}




class Base
