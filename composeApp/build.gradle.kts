@file:OptIn(ExperimentalKotlinGradlePluginApi::class, ExperimentalWasmDsl::class)

import io.github.fourlastor.construo.Target
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
    alias(libs.plugins.github.fourlastor.construo)
    id("dev.hydraulic.conveyor") version "1.12"
}

room {
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    jvmToolchain {
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(17)
    }
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
        wasmJsMain.dependencies { implementation(libs.jetbrains.browser) }
        jsCommonMain.dependencies {  }
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
//            implementation(libs.jna.core)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.jetbrains.jewel.decorated)
            // Conveyor API: Manage automatic updates.
            implementation("dev.hydraulic.conveyor:conveyor-control:1.1")
//            implementation(libs.flatlaf)
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

// region Work around temporary Compose bugs.
configurations.all {
    attributes {
        // https://github.com/JetBrains/compose-jb/issues/1404#issuecomment-1146894731
        attribute(Attribute.of("ui", String::class.java), "awt")
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
        versionName = properties["version.name.app"].toString()
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
java {
    toolchain {
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(17)
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
            targetFormats(º
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