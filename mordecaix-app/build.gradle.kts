import org.gradle.kotlin.dsl.getValue
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("app.base.application")
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.github.fourlastor.construo)
    alias(libs.plugins.conveyor)
}

composeApplication {
    config(
        versionCode = 1,
        versionName = "1.0.0",
        desktopMainClass = "MainKt",
        jsModuleName = Config.ApplicationName,
        jsOutputFileName = "${Config.ApplicationName}.js",
        desktopConfig = {
            this.application {
                this.buildTypes.release {
                    proguard {
                        this.optimize = false
                        this.obfuscate = true
                        this.configurationFiles.from(project.file("src/desktopMain/compose-desktop.pro"))
                    }
                }
                this.nativeDistributions {
                    this.modules(
                        modules = arrayOf(
                            "java.instrument",
                            "java.naming",
                            "java.sql",
                            "jdk.management",
                            "jdk.unsupported",
                            "java.net.http"
                        )
                    )
                    this.appResourcesRootDir = layout.projectDirectory.dir("src/desktopMain/assets")
                    this.targetFormats(
                        formats = arrayOf(
                            TargetFormat.Dmg,
                            TargetFormat.Msi,
                            TargetFormat.Deb,
                            TargetFormat.Exe,
                            TargetFormat.Rpm,
                            TargetFormat.Pkg
                        )
                    )
                    this@application.jvmArgs("--add-opens", "java.desktop/sun.awt=ALL-UNNAMED")
                    this@application.jvmArgs(
                        "--add-opens",
                        "java.desktop/java.awt.peer=ALL-UNNAMED"
                    )
                    this.macOS {
                        this.bundleID = Config.ApplicationId
                        this.appCategory = "public.app-category.developer-tools"
                        this.entitlementsFile.set(project.file("default.entitlements"))
                    }
                }
            }
        }
    )
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.compose.ui.tooling)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
        }

        nativeMain.dependencies { }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.jetbrains.jewel.decorated)
            implementation(libs.conveyor)
        }

        jsMain.dependencies { implementation(npm("is-sorted", "1.0.5")) }

        wasmJsMain.dependencies { }

        nonWebCommonMain.dependencies {
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite)
        }

        commonMain.dependencies {
            api(projects.mordecaixBase)
//            api(projects.mordecaixZipline)

            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.jetbrains.lifecycle.viewmodel)
            implementation(libs.jetbrains.lifecycle.runtime.compose)
            implementation(libs.jetbrains.compose.material.window)
            implementation(libs.jetbrains.compose.material.icons.core)
            implementation(libs.jetbrains.compose.material3.adaptive)
            implementation(libs.jetbrains.compose.material3.adaptive.layout)
            implementation(libs.jetbrains.compose.material3.adaptive.navigation)
            implementation(libs.jetbrains.androidx.navigation)
            implementation(libs.coil)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)
        }
    }
}

dependencies {
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

room { schemaDirectory("$projectDir/schemas") }