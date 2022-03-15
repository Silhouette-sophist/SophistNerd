package com.example.plugin_module;

import com.android.build.gradle.AppExtension
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * @author tgw
 * @date 2021/10/16
 * @describe
 *
 * //Asm的使用：https://www.jianshu.com/p/905be2a9a700
 * 使用asm提供的通过 ASMifier 自动生成对应的 ASM 代码。首先需要在ASM官网 下载 asm-all.jar 库，我下载的是最新的 asm-all-5.2.jar，然后使用如下命令，即可生成
 *  命令：
 * groovy -classpath E:\googleDowmload\asm-all-5.1.jar org.objectweb.asm.util.ASMifier E:\MyLearing\MyAptUseLearing\base-arouter\build\tmp\kotlin-classes\debug\com\example\base_arouter\ARouterUtils.class
 *
 * jar包下载地址：
 * http://nexus.neeveresearch.com/nexus/content/repositories/public/org/ow2/asm/asm-all/5.1/
 */
class MyPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        /*AppExtension android = (AppExtension) project.getExtensions().getByType(AppExtension.class);
        android.registerTransform(new ScanAllFileTransform(project));*/
        System.out.println("MyPlugin自定义独立插件0.................");

        project.afterEvaluate(new Action<Project>() {
            @Override
            void execute(Project pj) {

                AppExtension appExtension = project.getExtensions().getByType(AppExtension.class)
                println("$appExtension")
            }
        })
    }
}

