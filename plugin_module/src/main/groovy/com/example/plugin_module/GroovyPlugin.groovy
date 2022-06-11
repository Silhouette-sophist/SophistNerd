package com.example.plugin_module;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * 使用groovy定义的插件，使用groovy和gradle的api
 */
class GroovyPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        /*AppExtension android = (AppExtension) project.getExtensions().getByType(AppExtension.class);
        android.registerTransform(new ScanAllFileTransform(project));*/
        System.out.println("GroovyPlugin executed......");

        project.afterEvaluate(new Action<Project>() {
            @Override
            void execute(Project pj) {

                println "GroovyPlugin afterEvaluate......"
            }
        })
    }
}

