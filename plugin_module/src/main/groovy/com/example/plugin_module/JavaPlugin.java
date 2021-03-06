package com.example.plugin_module;

import com.example.plugin_module.helper.TimeFormat;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * 使用java定义的插件，使用groovy和gradle的api
 */
public class JavaPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        System.out.println("JavaPlugin apply......" + TimeFormat.getCurrentTime());

        project.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(Project project) {
                System.out.println("JavaPlugin afterEvaluate......" + TimeFormat.getCurrentTime());
            }
        });
    }
}
