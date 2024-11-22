@file:OptIn(ExperimentalKotlinGradlePluginApi::class, ExperimentalWasmDsl::class)

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyTemplate
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.androidx.room)
}

room {
    schemaDirectory("$projectDir/schemas")
}

kotlin {

    applyHierarchyTemplate(KotlinHierarchyTemplate {
        withSourceSetTree(
            KotlinSourceSetTree.main,
            KotlinSourceSetTree.test,
        )
        common {
            withCompilations { true }
            group("nonAndroid") {
                withJvm()
                withJs()
                withWasmJs()
                group("native") {
                    group("apple") {
                        group("ios") { withIos() }
                        group("macos") { withMacos() }
                        withApple()
                    }
                    withNative()
                }
            }
            group("nonJs") {
                withJvm()
                withAndroidTarget()
                group("native") {
                    group("apple") {
                        group("ios") { withIos() }
                        group("macos") { withMacos() }
                        withApple()
                    }
                    withNative()
                }
            }
            group("jsCommon") {
                withJs()
                withWasmJs()
            }
        }
    })

    androidTarget()

    wasmJs {
        moduleName = "ComposeApp"
        browser {
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                /*devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        add(projectDirPath)
                    }
                }*/
            }
        }
        binaries.executable()
    }

    js {
        moduleName = "ComposeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            linkerOpts.add("-lsqlite3") // Required when using NativeSQLiteDriver
        }
    }

    sourceSets {
        val desktopMain by getting
        val commonMain by getting
        val nonJsMain by getting
        val nonAndroidMain by getting
        val jsCommonMain by getting
        val wasmJsMain by getting
        wasmJsMain.dependencies {  }
        jsCommonMain.dependencies { implementation(libs.jetbrains.browser) }
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.koin.android)
        }
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.jetbrains.kotlinx.coroutines)
            implementation(libs.jetbrains.kotlinx.collections)
            implementation(libs.jetbrains.lifecycle.viewmodel)
            implementation(libs.jetbrains.lifecycle.runtime.compose)
            implementation(libs.jetbrains.compose.materialWindow)
            implementation(libs.jetbrains.compose.material3.adaptive)
            implementation(libs.jetbrains.compose.material3.adaptive.layout)
            implementation(libs.jetbrains.compose.material3.adaptive.navigation)
            implementation(libs.jetbrains.androidx.navigation)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.coil)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)
            implementation(libs.ktor.client.core)

            implementation(libs.kotlinx.datetime)

            implementation(libs.haze)
            implementation(libs.haze.materials)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.kotlinx.coroutines.swing)
        }
        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        nonJsMain.dependencies {
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite)
            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.core)
            implementation(libs.androidx.datastore.preference)
        }
        nonAndroidMain.dependencies {
            implementation(libs.skiko)
        }
    }
}

android {
    namespace = "com.crow.mordecaix"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.crow.mordecaix"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(libs.androidx.compose.ui.tooling)
    }
}

compose.desktop {
    application {
        mainClass = "com.crow.mordecaix.MainKt"
        nativeDistributions {
            appResourcesRootDir = layout.projectDirectory.dir("src/desktopMain/assets")
            jvmArgs += "-splash:${'$'}APPDIR/resources/splash.png"
            targetFormats(
                TargetFormat.Dmg,
                TargetFormat.Msi,
                TargetFormat.Deb,
                TargetFormat.Exe,
//                TargetFormat.AppImage,
                TargetFormat.Rpm,
                TargetFormat.Pkg
            )
            packageName = "com.crow.mordecaix"
            packageVersion = "1.0.0"
        }
    }
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspDesktop", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}

// https://youtrack.jetbrains.com/issue/KT-56025
afterEvaluate {
    tasks {
        val configureJs: Task.() -> Unit = {
            dependsOn(named("jsDevelopmentExecutableCompileSync"))
            dependsOn(named("jsProductionExecutableCompileSync"))
            dependsOn(named("jsTestTestDevelopmentExecutableCompileSync"))
        }
        named("jsBrowserProductionWebpack").configure(configureJs)
    }
}
// https://youtrack.jetbrains.com/issue/KT-56025
afterEvaluate {
    tasks {
        val configureWasmJs: Task.() -> Unit = {
            dependsOn(named("wasmJsDevelopmentExecutableCompileSync"))
            dependsOn(named("wasmJsProductionExecutableCompileSync"))
            dependsOn(named("wasmJsTestTestDevelopmentExecutableCompileSync"))
        }
        named("wasmJsBrowserProductionWebpack").configure(configureWasmJs)
    }
}