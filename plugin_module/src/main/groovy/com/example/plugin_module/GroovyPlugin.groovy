package com.example.plugin_module

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.AppExtension;
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

        /*AppExtension android = (AppExtension) project.getExtensions().getByType(AppExtension.class)
        android.registerTransform(new Transform() {
            @Override
            String getName() {
                return "GroovyPluginTransform"
            }

            @Override
            Set<QualifiedContent.ContentType> getInputTypes() {
                return null
            }

            @Override
            Set<? super QualifiedContent.Scope> getScopes() {
                return null
            }

            @Override
            boolean isIncremental() {
                return false
            }

            @Override
            void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
                println("GroovyPluginTransform.....transforming...." +TimeFormat.getCurrentTime())
                Thread.sleep(5000)
                println("GroovyPluginTransform.....transformed...." +TimeFormat.getCurrentTime())
            }
        })*/
    }
}

