import app.cash.zipline.gradle.ZiplineServeTask
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.zipline)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.mordecai"
version = "1.0.0"

kotlin {
    applyDefaultHierarchyTemplate()
    androidTarget()
    js {
        browser()
        binaries.executable()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.zipline)
                implementation(libs.ktor.client.core)
            }
        }
        val hostMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.zipline.loader)
                api(libs.okio.core)
            }
        }
        val androidMain by getting {
            dependsOn(hostMain)
            dependencies {
                implementation(libs.ktor.client.okhttp)
                implementation(libs.okhttp)
            }
        }
    }
}

android {
    compileSdk = 36
    namespace = "com.mordecai.zipline.presenter"
    defaultConfig { minSdk = 23 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

zipline {
    mainFunction.set("com.mordecai.zipline.main")
    httpServerPort.set(8000)
}
tasks.named<ZiplineServeTask>("serveDevelopmentZipline") {
    port
    inputDir
}

plugins.withType<YarnPlugin> {
    the<YarnRootExtension>().yarnLockAutoReplace = true
}
