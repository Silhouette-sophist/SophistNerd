# 工程说明
主要是为了重现整理一下Android知识，基于新技术进行使用，从Kotlin、Jetpack、Compose到Flutter等技术。
另外，也会使用常见的架构技术，依赖注入、Okhttp、Retrofit、RxJava等。另外，另外还会研究字节码插桩技术，ASM等。


## apk输出位置变化
在新版本的Android Studio中编译apk，并不是在YourProject\app\build\outputs\apk\debug，
而是更改到了YourProject\app\build\intermediates\apk/debug\app-debug.apk

最佳做法：
- 菜单依次“Build”——“Build Bundle(s) / APK(s)”——“Build APK(s)”
- 等待生成应用完成，IDE右下角会提示

## 提交分支限制方法



## 编译依赖限制，特定版本打包

- gradle命令添加参数

```groovy
./gradlew assemble -Psome=true
```

- build.gradle参数获取

在build.gradle文件中直接使用project获取属性即可，可以通过这里来动态添加依赖，比如一些测试包
```groovy
if (project.hasProperty("hello")) {
    println "*******hasProperty hello*******"
} else {
    println "*******hasn't Property hello*******"
}
```

- 代码中按照反射使用

通过反射调用指定版本的测试类，因为是非必须导入的，所以IDE无法提示，只能够反射了。 并通过反射调用类的方法。


## 依赖注入模块

@Inject：在构造函数、字段、方法前使用，表示需要注入依赖才可以正常使用
- Spring自带的@Autowired的缺省情况等价于JSR-330的@Inject注解；

@Qualifier：
- Spring自带的@Qualifier的扩展@Qualifier限定描述符注解情况等价于JSR-330的@Qualifier注解。

@Named：属于Qualifier的一种
- Spring自带的@Qualifier的缺省的根据Bean名字注入情况等价于JSR-330的@Named注解；

@Provider

@Scope

@Singleton：属于Scope的一种

## Kotlin协程

## adb端口映射

## groovy迁移到kotlin


## Dagger2依赖注入

- 依赖库
```groovy
implementation 'com.google.dagger:dagger:2.19'
annotationProcessor 'com.google.dagger:dagger-compiler:2.19'
```

- 创建提供依赖的类

无参构造函数上使用了 @Inject注解，告诉 Dagger 2 这个无参构造函数可以被用来创建 Cat 对象，即依赖的提供方
```java
public class Cat {
    @Inject
    public Cat() {
    }

    @Override
    public String toString() {
        return "喵星人来了!";
    }
}
```

- 创建使用依赖对象的类

在 CacheActivity 中创建了一个 cat变量，并加上了 @Inject注解，来告诉 Dagger2 你要为cat赋值，即依赖注入。所以 MainActivity 就是依赖的需求方。

```kotlin
class CacheActivity : AppCompatActivity() {

    @Inject
    lateinit var cat : Cat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cache_activity)
        
        println("cat is injected $cat")
    }
}
```

- 创建依赖注入组件

现在依赖的提供方 Cat 类有了，依赖的需求方 CacheActivity 也有了，那么如何将依赖的提供方和需求方关联起来呢。类似于我们日常的购物，卖家和买家需要通过电商平台的中介来完成供需信息的交换，完成最终的交易。

在 Dagger 2 中也有类似的中介，那就是Component，它负责完成依赖注入的过程，我们可以叫它依赖注入组件，大致的意思就是依赖需求方需要什么类型的对象，依赖注入组件就从依赖提供方中拿到对应类型的对象，然后进行赋值。

```kotlin
@Component
interface MainComponent {

    fun inject(cacheActivity: CacheActivity)
}
```

inject方法的参数是依赖需求方的类型，即例子中的 MainActivity，注意不可是基类的类型。

但这只是个接口，没法直接使用呀，肯定还需要具体的依赖注入逻辑的，当然，但这些工作框架会帮我们做的。我们前边说过 Dagger 2 采用了annotationProcessor技术，在项目编译时动态生成依赖注入需要的 Java 代码。

此时我们编译项目，由于使用了@Component注解，框架会自动帮我们生成一个MainComponent接口的实现类DaggerMainComponent，这个类可以在app\build\generated\source\apt\debug\包名目录下找到。

所以，DaggerMainComponent就是真正的依赖注入组件，最后在 CacheActivity 中添加最终完成依赖注入的代码：

```kotlin
class CacheActivity : AppCompatActivity() {

    @Inject
    lateinit var cat : Cat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cache_activity)

        DaggerMainComponent.builder()
            .build()
            .inject(this);

        println("cat is injected $cat")
    }
}
```

- 通用的依赖提供方

前边我们的 Cat 类并没有带参的构造函数，可以直接在其无参的构造函数使用@Inject注解，进而当做依赖提供方来提供对象。但实际的情况可能并没有这么简单，可能构造函数需要参数，或者类是第三方提供的我们无法修改等，导致我们无法使用@Inject，这些情况下就需要我们自定义依赖提供方了。

在 Dagger 2 中，如果一个类使用了@Module注解，那么这个类就可以用来提供依赖对象：



- 窗口类型