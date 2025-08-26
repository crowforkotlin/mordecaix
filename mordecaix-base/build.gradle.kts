@file:OptIn(ExperimentalKotlinGradlePluginApi::class, ExperimentalWasmDsl::class)
@file:Suppress("UnstableApiUsage")

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyTemplate
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
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

/*    jvmToolchain {
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(17)
    }*/

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

            }
        }
        val desktopMain by getting {

        }
        val commonMain by getting {
            dependencies {
                api(libs.kotlin.stdlib)

                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.material3)
                api(compose.ui)
                api(compose.components.resources)
                api(compose.components.uiToolingPreview)

                api(libs.jetbrains.kotlinx.coroutines)
                api(libs.jetbrains.kotlinx.collections)
                api(libs.jetbrains.lifecycle.viewmodel)
                api(libs.jetbrains.lifecycle.runtime.compose)
                api(libs.jetbrains.compose.material.window)
                api(libs.jetbrains.compose.material.icons)
                api(libs.jetbrains.compose.material3.adaptive)
                api(libs.jetbrains.compose.material3.adaptive.layout)
                api(libs.jetbrains.compose.material3.adaptive.navigation)
                api(libs.jetbrains.androidx.navigation)
                api(libs.koin.core)
                api(libs.koin.compose)
                api(libs.coil)
                api(libs.koin.compose.viewmodel)
                api(libs.koin.compose.viewmodel.navigation)
                api(libs.ktor.client.core)

                api(libs.kotlinx.datetime)

                api(libs.haze)
                api(libs.haze.materials)
            }
        }
        val nonJsMain by getting {
            dependencies {

            }
        }
        val nonAndroidMain by getting {
            dependencies {
            }
        }
        val nativeMain by getting {
            dependencies {
            }
        }
        val wasmJsMain by getting {
            dependencies  {

            }
        }
        val jsMain by getting {

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
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
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
        debugApi(libs.androidx.compose.ui.tooling)
    }
}
/*java {
    toolchain {
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(17)
    }
}*/
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