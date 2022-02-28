# 偏函数的使用（函数柯里化）
对一个多参数的函数，通过指定其中的一部分参数后得到的仍然是一个函数，那么这个函数就是原函数的一个偏函数了。
```kotlin
//被调用函数，待传递函数。需要两个参数，一个是定位的view，一个是消息体。前者应该是ui层传递，后者应该是业务完成后的回调。
@MainThread
fun showSnackbarMessage(anchorView: View, msg: String) {

    if (anchorView.isAttachedToWindow) {
        Snackbar.make(anchorView, msg, BaseTransientBottomBar.LENGTH_SHORT)
            .setAction("确定") {

            }
            .show()
    }
}

//调用函数，这是业务层定义，需要在业务完成后回调传递的接口
fun previous(callback: ((String) -> Unit)? = null) {
    imageSource.value?.let {
        if (it.size > 0) {
            index--
            index = abs(index % it.size)
            currentImage.value = it[index]
            "previous image : $index / ${it.size}".also { msg ->
                callback?.invoke(msg)
                logger.info(msg)
            }
        }
    }
}

//1.增加偏函数支持！！！方便传递函数参数。
//将偏函数作为扩展函数定义出来
fun <P1, P2, R> Function2<P1, P2, R>.partial1(p1: P1) = fun(p2: P2) = this(p1, p2)
fun <P1, P2, R> Function2<P1, P2, R>.partial2(p2: P2) = fun(p1: P1) = this(p1, p2)
//按照定义的偏函数使用
savedStateViewModel.search(text.trim().toString(), ::showSnackbarMessage.partial1(it))


//2.匿名函数实现
savedStateViewModel.previous(fun(msg : String){
    showSnackbarMessage(it, msg = msg)
})


//3.lambda表达式实现
savedStateViewModel.next { msg : String ->
    showSnackbarMessage(it, msg)
}
```

# 扩展函数和常规函数的关系
下面两个定义同时出现，编译器会直接报错，说两个函数的签名是一样的。

```kotlin
//扩展函数定义
fun String.appendMessage(message: String) = "$this  $message"
//常规函数定义
fun appendMessage(firstMessage : String, secondMessage : String) = "$firstMessage $secondMessage"
```
![img.png](img.png)

针对上面的情况，可以通过以下操作来放大问题：
```kotlin
fun main() {
    //通过提示可以看出tagFunction类型是KFunction2<String,String,String>
    var tagFunction = ::appendMessage
    //下面操作也是合法的
    tagFunction = String::appendMessage
}
```
实际上，扩展函数和常规函数可以相互赋值，因为其函数原型是一样的，如上例子类型是KFunction2<String,String,String>


# lambda表达式再思考
lambda表达式特征:
- 定义类型时以(ParameterType) -> ReturnType 
- 定义实现时以{(ParameterInstance) -> Implementation}

```kotlin
fun main() {
    //定义参数类型
    var lambdaType : (String, String) -> String = ::appendMessage
    //定义函数实现
    lambdaType = {(firstMessage, secondMessage) -> {
        "$firstMessage $secondMessagebushi"
    }}
}
```

再往前走一步，我们常见的情况：
```kotlin
//lambda作为参数传递给函数
savedStateViewModel.next({ msg : String ->
    showSnackbarMessage(it, msg)
})

//lambda作为函数最后一个参数，可以将lambda表达式从函数参数中提取出来，直接放到后面
savedStateViewModel.next { msg : String ->
    showSnackbarMessage(it, msg)
}
```

这里想要说明的一点就是：
- lambda作为函数最后一个参数，可以将lambda表达式从函数参数中提取出来，直接放到后面
- 如果函数后直接接了{}，则说明函数接受的是lambda表达式。并且，如果lambda表达式只有一个参数又可以省去形参，使用it作为形参！！！

# FunctionInterface和lambda
在Java中，如果一个接口只有一个抽象方法，那么可以直接用lambda表达式表示这个接口。

Java风格的kotlin写法如下：
```kotlin
interface MyInterface {
    fun show()
}

fun callShow(myInterface: MyInterface) {
    myInterface.show()
}

fun main() {
    callShow(object : MyInterface {
        override fun show() {
            TODO("Not yet implemented")
        }
    })

    //但是这里就无法调用了！！！
    callShow {
        println("Single Abstract Method interfaces")
    }
}
```

在Kotlin中为了实现相同的FuctionInterface转lambda表达式的操作，可以将MyInterface调整为下面的定义：
```kotlin
//注意fun interface的变化
fun interface MyInterface {
    fun show()
}

fun callShow(myInterface: MyInterface) {
    myInterface.show()
}

fun main() {
    callShow(object : MyInterface {
        override fun show() {
            TODO("Not yet implemented")
        }
    })

    //这里可以正常调用了！！！
    callShow {
        println("Single Abstract Method interfaces")
    }
}
```

所以，在Kotlin中，函数式接口完全可以直接定义为lambda表达式，或者定义为kotlin风格的function interface：
```kotlin
//1.Java风格的函数时接口
interface MyInterface {
    fun show()
}

//2.lambda表达式定义
//定义参数类型， () -> Unit
//定义函数实现
{(firstMessage, secondMessage) -> {
    "$firstMessage $secondMessagebushi"
}}

fun lambdaInterface() {
    println("lambdaInterface")
}

fun main() {
    //fuction interface的lambda表达式形式
    callShow {
        println("function interface")
    }
    //函数类型调用
    callShow(::lambdaInterface)
}

//3.kotlin风格的fuction interface定义
fun interface MyInterface {
    fun show()
}
```

对于，callShow直接传递lambda表达式的理解，按照两步走：
- 函数式接口转化为lambda表达式， MyInterface直接转换为() -> Unit类型
- lambda表达式的函数类型，() -> Unit类型正好表示不接受参数，也不返回的函数类型

所以，后续对于callShow这种需要函数式接口对象的调用，直接按照lambda表达式方式调用即可！！！
并以函数类型，实参类型，返回类型去定义lambda表达式即可。
