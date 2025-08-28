import extensions.ApplicationExtension

plugins {
    id("com.android.application")
}

/**
 * 该插件封装了大部分模版配置，其余配置由 [ApplicationExtension] 配置
 */

extensions.create("composeApplication", ApplicationExtension::class.java, project)

android {
    namespace = Config.getNamespace(project)
    compileSdk = libsEx.versions.`android-compileSdk`.requiredVersion.toInt()
    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res")
            resources.srcDirs("src/commonMain/resources")
        }
    }
    defaultConfig {
        applicationId = "com.crow.mordecaix"
        minSdk = libsEx.versions.`android-minSdk`.requiredVersion.toInt()
        targetSdk = libsEx.versions.`android-targetSdk`.requiredVersion.toInt()
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
        debugImplementation(libsEx.`androidx-compose-ui-tooling`)
    }
}