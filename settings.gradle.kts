rootProject.name = "mordecaix"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
//        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
//        gradlePluginPortal()
        maven("https://packages.jetbrains.team/maven/p/kpm/public/") {
            mavenContent {
                includeGroupByRegex(".*java.*")
            }
        }
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev") {
            mavenContent {
                includeGroupByRegex(".*java.*")
            }
        }
        maven("https://packages.jetbrains.team/maven/p/kpm/public") {
            mavenContent {
                includeGroupAndSubgroups("org.jetbrains.jewel")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")