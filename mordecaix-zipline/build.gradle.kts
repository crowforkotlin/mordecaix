import com.android.tools.r8.internal.de
import org.gradle.kotlin.dsl.androidMain
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.jsMain
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    id("app.base.library")
    id("app.base.multiplatform")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    applyBaseHierarchyTemplate()
    sourceSets {

    }
}