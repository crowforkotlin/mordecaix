@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyBuilder
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyTemplate
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

val KotlinMultiplatformExtension.nonWebCommonMain: KotlinSourceSet get() {
    return this.sourceSets.getByName("nonWebCommonMain")
}

fun KotlinMultiplatformExtension.applyBaseHierarchyTemplate() {
    this.applyHierarchyTemplate(template = KotlinHierarchyTemplate {
        this.withSourceSetTree(tree = arrayOf(KotlinSourceSetTree.main, KotlinSourceSetTree.test))
        this.common {
            this.withCompilations { true }
            this.native()
            this.nonWebCommon()
            this.webCommon()
        }
    })
}
private fun KotlinHierarchyBuilder.native() {
    group("native") {
        group("apple") {
            group("ios") { withIos() }
            group("macos") { withMacos() }
            withApple()
        }
        withNative()
    }
}

private fun KotlinHierarchyBuilder.webCommon() {
    group(name = "webCommon") {
        withJs()
        withWasmJs()
    }
}

private fun KotlinHierarchyBuilder.nonWebCommon() {
    group(name = "nonWebCommon") {
        withJvm()
        withAndroidTarget()
        native()
    }
}
