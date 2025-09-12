plugins {
    alias(libs.plugins.kotlin.android)
    id("app.base.android")
}

androidApplication {
    config(
        versionCode = 1,
        versionName = "1.0.0-release"
    ) {
        implementation(libs.androidx.core.ktx)
    }
}