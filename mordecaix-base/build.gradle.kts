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

        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        jsMain.dependencies {
            implementation(libs.kotlin.stdlib.js)
        }

        wasmJsMain.dependencies {
            implementation(libs.jetbrains.browser)
        }

        nonWebCommonMain. dependencies {
            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.core)
            implementation(libs.androidx.datastore.preference)
        }
    }
}