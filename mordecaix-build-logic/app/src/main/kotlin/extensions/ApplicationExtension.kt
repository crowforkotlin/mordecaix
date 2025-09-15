@file:OptIn(ExperimentalWasmDsl::class, ExperimentalKotlinGradlePluginApi::class)

package extensions

import applyBaseHierarchyTemplate
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyBuilder
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyTemplate
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmTargetDsl

abstract class ApplicationExtension(val project: Project) {

  fun config(
    versionCode: Int,
    versionName: String,
    desktopMainClass: String,
    jsModuleName: String,
    jsOutputFileName: String,
    desktopConfig: DesktopExtension.() -> Unit = {},
    wasmJsConfig: KotlinWasmJsTargetDsl.() -> Unit = {},
    jsConfig: KotlinJsTargetDsl.() -> Unit = {},
  ) {
    project.configure<KotlinMultiplatformExtension> {
      this.applyBaseHierarchyTemplate()
      this.js {
        this.jsConfig()
        this.outputModuleName.set(jsModuleName)
        this.browser { this.commonWebpackConfig { this.outputFileName = jsOutputFileName } }
      }
      /*this.wasmJs {
        project.group = Config.Group
        this.wasmJsConfig()
        this.outputModuleName.set(jsModuleName)
        this.browser { this.commonWebpackConfig { this.outputFileName = jsOutputFileName } }
      }*/
    }
    project.configure<BaseAppModuleExtension> {
      defaultConfig {
        this.versionCode = versionCode
        this.versionName = versionName
      }
    }
    project.configure<ComposeExtension> {
      extensions.configure<DesktopExtension> {
        this.desktopConfig()
        application {
          mainClass = desktopMainClass
          nativeDistributions {
            packageVersion = versionName
          }
        }
      }
    }
  }


}