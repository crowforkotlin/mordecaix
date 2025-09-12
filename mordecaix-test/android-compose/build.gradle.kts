plugins {
    alias(libs.plugins.kotlin.android)
    id("app.base.android.compose")
    id("app.function.android.compose")
}

androidApplication {
    config(
        versionCode = 1,
        versionName = "1.0.0-release"
    )
}