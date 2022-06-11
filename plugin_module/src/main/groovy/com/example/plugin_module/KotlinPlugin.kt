package com.example.plugin_module

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 使用kotlin定义的插件，使用groovy和gradle的api
 * 目前无法编译，因为srcSource是groovy
 */
class KotlinPlugin : Plugin<Project> {
    override fun apply(p0: Project) {
        println("KotlinPlugin apply......")
        p0.afterEvaluate {
            println("KotlinPlugin afterEvaluate......")
        }
    }
}