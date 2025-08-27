@file:OptIn(ExperimentalKotlinGradlePluginApi::class, ExperimentalWasmDsl::class)
@file:Suppress("unused")

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyTemplate
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.github.fourlastor.construo)
    id("dev.hydraulic.conveyor") version "1.12"
}

java {
    toolchain {
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(17)
    }
}

kotlin {

    jvmToolchain {
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(17)
    }

    applyHierarchyTemplate(template = KotlinHierarchyTemplate {
        withSourceSetTree(tree = arrayOf(KotlinSourceSetTree.main, KotlinSourceSetTree.test))
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
        moduleName = "mordecaix"
        browser {
            testTask {
                enabled = false
            }
            commonWebpackConfig {
                outputFileName = "mordecaix.js"
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
        moduleName = "mordecaix"
        browser {
            testTask {
                enabled = false
            }
            commonWebpackConfig {
                outputFileName = "mordecaix.js"
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
            baseName = "mordecaix"
            isStatic = true
            linkerOpts.add("-lsqlite3") // Required when using NativeSQLiteDriver
        }
    }

    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)
                implementation(libs.kotlinx.coroutines.android)
                implementation(libs.kotlinx.coroutines.guava)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.koin.android)
            }
        }
        val nativeMain by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.jetbrains.jewel.decorated)
                implementation(libs.conveyor)
            }
        }
        val commonMain by getting {
            dependencies {
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
                implementation(libs.jetbrains.compose.material.window)
                implementation(libs.jetbrains.compose.material.icons)
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
        }
        val nonJsMain by getting {
            dependencies {
                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.sqlite)
                implementation(libs.androidx.datastore)
                implementation(libs.androidx.datastore.core)
                implementation(libs.androidx.datastore.preference)
            }
        }
        val nonAndroidMain by getting {
            dependencies {
                implementation(libs.skiko)
            }
        }
        val wasmJsMain by getting {
            dependencies {
                implementation(libs.jetbrains.browser)
            }
        }
        val jsMain by getting {
            dependencies {
//                implementation(npm("is-sorted", "1.0.5"))
//                implementation(libs.kotlin.stdlib.js)
            }
        }
        val jsCommonMain by getting
    }
}

android {
    namespace = "com.crow.mordecaix"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res")
            resources.srcDirs("src/commonMain/resources")
        }
    }
    defaultConfig {
        applicationId = "com.crow.mordecaix"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = properties["version.name.app"].toString()
    }
    packaging {
        resources {
            pickFirsts += arrayOf(
                "META-INF/androidx.compose.ui_ui.version"
            )
            excludes += arrayOf(
                "DebugProbesKt.bin",
                "kotlin-tooling-metadata.json",
                "kotlin/**",
                "META-INF/*.version",
                "META-INF/**/LICENSE.txt",
                "/META-INF/{AL2.0,LGPL2.1}"
            )
        }
        dex {
            useLegacyPackaging = true
        }
        jniLibs {
            useLegacyPackaging = true
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
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
    group = "com.crow.mordecaix.desktop"
    version = "1.0.0"
    application {
        mainClass = "com.crow.mordecaix.MainKt"
//        javaHome = "/Users/crowforkotlin/Downloads/jbr_jcef-17.0.11-osx-aarch64-b1312.2/Contents/Home/"
        javaHome = "/Library/Java/JavaVirtualMachines/jdk-17.0.1.jdk/Contents/Home/"
        buildTypes.release {
            proguard {
                this.optimize = false
                this.obfuscate = true
                this.configurationFiles.from(project.file("src/desktopMain/compose-desktop.pro"))
            }
        }
        nativeDistributions {
            modules(
                "java.instrument",
                "java.naming",
                "java.sql",
                "jdk.management",
                "jdk.unsupported",
                "java.net.http",
            )
            appResourcesRootDir = layout.projectDirectory.dir("src/desktopMain/assets")
            targetFormats(
                TargetFormat.Dmg,
                TargetFormat.Msi,
                TargetFormat.Deb,
                TargetFormat.Exe,
                TargetFormat.Rpm,
                TargetFormat.Pkg
            )
            packageName = "mordecaix"
            packageVersion = properties["version.name.desktop"].toString()
            jvmArgs("--add-opens", "java.desktop/sun.awt=ALL-UNNAMED")
            jvmArgs("--add-opens", "java.desktop/java.awt.peer=ALL-UNNAMED")
            if (System.getProperty("os.name").contains("Mac")) {
                jvmArgs("--add-opens", "java.desktop/sun.lwawt=ALL-UNNAMED")
                jvmArgs("--add-opens", "java.desktop/sun.lwawt.macosx=ALL-UNNAMED")
            }
            macOS {
                bundleID = "com.crow.mordecaix"
                mainClass = "com.crow.mordecaix.MainKt"
                appCategory = "public.app-category.developer-tools"
                javaHome = "/Library/Java/JavaVirtualMachines/jdk-17.0.1.jdk/Contents/Home/"
                entitlementsFile.set(project.file("default.entitlements"))
            }
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

    linuxAmd64(compose.desktop.linux_x64)
    macAmd64(compose.desktop.macos_x64)
    macAarch64(compose.desktop.macos_arm64)
    windowsAmd64(compose.desktop.windows_x64)
}

room {
    schemaDirectory("$projectDir/schemas")
}
