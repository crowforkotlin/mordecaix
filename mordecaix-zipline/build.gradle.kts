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
        val desktopMain by getting {
            dependsOn(hostMain)
        }
        val jsMain by getting {
            dependsOn(hostMain)
            dependencies {
            }
        }
    }
}

zipline {
    mainFunction.set("com.crow.mordecaix.zipline.main")
    httpServerPort.set(8000)
}

plugins.withType<YarnPlugin> {
    the<YarnRootExtension>().yarnLockAutoReplace = true
}
