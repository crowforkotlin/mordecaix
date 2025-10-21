@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("app.base.library")
    id("app.base.multiplatform")
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    applyBaseHierarchyTemplate()
    sourceSets {
        commonMain.dependencies {

            api(libs.kotlin.stdlib)

            api(libs.kotlinx.serialization.json)
            api(libs.kotlinx.datetime)

            api(libs.haze)
            api(libs.haze.materials)

            api(libs.ktor.client.core)

            api(libs.kotlinx.coroutines)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        desktopMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        /*nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }*/
        nonWebCommonMain.dependencies {
            api(libs.androidx.room.runtime)
            api(libs.androidx.sqlite)
            api(libs.androidx.datastore)
            api(libs.androidx.datastore.core)
            api(libs.androidx.datastore.preference)
        }
    }
}