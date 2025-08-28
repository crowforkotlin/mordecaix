package extensions

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.desktop.DesktopExtension

abstract class ApplicationExtension(val project: Project) {

  fun config(
    versionCode: Int,
    versionName: String,
    desktopMainClass: String,
  ) {
    project.configure<BaseAppModuleExtension> {
      defaultConfig {
        this.versionCode = versionCode
        this.versionName = versionName
      }
    }
    project.configure<ComposeExtension> {
      extensions.configure<DesktopExtension> {
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