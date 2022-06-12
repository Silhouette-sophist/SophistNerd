package com.example.plugin_module


import com.android.build.gradle.AppExtension
import com.example.plugin_module.helper.TimeFormat
import com.example.plugin_module.transform.InjectTransform;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * 使用groovy定义的插件，使用groovy和gradle的api
 */
class GroovyPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        System.out.println("GroovyPlugin executed......" + TimeFormat.getCurrentTime())

        project.afterEvaluate(new Action<Project>() {
            @Override
            void execute(Project pj) {

                println "GroovyPlugin afterEvaluate......" + TimeFormat.getCurrentTime()
            }
        })

        AppExtension android = (AppExtension) project.getExtensions().getByType(AppExtension.class)
        //android.registerTransform(new InjectTransform(project))
    }
}

