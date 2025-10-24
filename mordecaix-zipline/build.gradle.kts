@file:Suppress("OPT_IN_USAGE")

import org.jetbrains.compose.internal.utils.localPropertiesFile
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

plugins {
    id("app.base.library")
    id("app.base.multiplatform")
    alias(libs.plugins.zipline)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.mordecaixZiplineCommon)
                implementation(libs.ktor.client.core)
            }
        }
        val hostMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.zipline.loader)
                implementation(libs.koin.core)
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
        val desktopMain by getting {
            dependsOn(hostMain)
            dependencies {
                implementation(libs.log4j2.core)
                implementation(libs.log4j2.api)
                implementation(libs.log4j2.slf4j)
                implementation(libs.slf4j)
            }
        }
        val jsMain by getting {
            dependsOn(commonMain)
            dependencies {
            }
        }
    }
}

zipline {
    mainFunction.set("com.crow.mordecaix.zipline.main")
    httpServerPort.set(8000)
    signingKeys {
        create("key") {
            
            privateKeyHex.set("f14924dee19242c1b16113ff52ac366607882b74b132266af6c52c56879c9bc7")
            algorithmId.set(app.cash.zipline.loader.SignatureAlgorithmId.Ed25519)
        }
    }
}

plugins.withType<YarnPlugin> {
    the<YarnRootExtension>().yarnLockAutoReplace = true
}
