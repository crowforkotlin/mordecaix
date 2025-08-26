@file:OptIn(ExperimentalKotlinGradlePluginApi::class, ExperimentalWasmDsl::class)
@file:Suppress("UnstableApiUsage")

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
    alias(libs.plugins.conveyor)
}

room {
    schemaDirectory("$projectDir/schemas")
}

kotlin {

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
        outputModuleName = "mordecaix"
        useCommonJs()
        browser {
            commonWebpackConfig {
                outputFileName = "mordecaix-app.js"
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
        outputModuleName = "mordecaix"
        useCommonJs()
        browser {
            commonWebpackConfig {
                outputFileName = "mordecaix-app.js"
            }
        }
        binaries.executable()
    }

    jvm(name = "desktop")

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
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.jetbrains.jewel.decorated)

                // Conveyor API: Manage automatic updates.
                implementation(libs.conveyor.control)
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(projects.mordecaixBase)
                implementation(libs.jetbrains.compose.material.icons)
            }
        }
        val nonAndroidMain by getting {
            dependencies {
//                implementation(libs.skiko)
            }
        }
        val nativeMain by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
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

        val jsCommonMain by getting { }
        val wasmJsMain by getting {
            dependencies {
                implementation(libs.jetbrains.browser)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(npm("is-sorted", "1.0.5"))
                implementation(libs.kotlin.stdlib.js)
            }
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
            pickFirsts += arrayOf(
                "META-INF/androidx.compose.ui_ui.version"
            )
            excludes += arrayOf(
                "DebugProbesKt.bin",
                "kotlin-tooling-metadata.json",
                "kotlin/**",
                "META-INF/*.version",
                "META-INF/**/LICENSE.txt"
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
        getByName("release") {
            isShrinkResources = true
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

compose.resources {
    publicResClass = true
    packageOfResClass = "com.crow.mordecaix.resources"
    generateResClass = always
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