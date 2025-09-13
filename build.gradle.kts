plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.zipline) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.github.fourlastor.construo) apply false
    alias(libs.plugins.conveyor) apply false
    alias(libs.plugins.kotlin.android) apply false
}

project.extra["pluginDataStoreInputDirPath"] = file("mordecaix-app/datastore").absolutePath
project.extra["pluginDataStoreOutputDirPath"] = file("mordecaix-app/datastore-source").absolutePath

apply(from = "gradle/plugin.datastore.gradle.kts")


/*

tasks.register<DefaultTask>("decodePreferencesPb") {
    val protocPath = "protoc"
    val dir = File("${System.getProperty("user.dir")}/datastore_temp")
    val inputDir = File("D:\\fish\\MordecaiX\\composeApp\\datastore")
    if (!dir.exists()) dir.mkdir()

    inputDir.listFiles()?.forEach { file ->
        if (file.name.endsWith(".preferences_pb")) {
            val command = listOf(protocPath, "--decode_raw")
            val processBuilder = ProcessBuilder(command)
            processBuilder.redirectErrorStream(true)
            val process = processBuilder.start()
            FileInputStream(file).use { input ->
                process.outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            val result = process.inputStream.bufferedReader().readText()
            val exitCode = process.waitFor()
            if (exitCode != 0) {
                throw GradleException("protoc command failed with exit code $exitCode")
            }
            val outputFile = File(dir, "${file.nameWithoutExtension}_datastore.txt")
            outputFile.writeText(result)
        }
    }
}

*/

fun Project.kotlinDependenciesConfig() {
    dependencies {
        modules {
            module("org.jetbrains.kotlin:kotlin-stdlib-jdk7") {
                replacedBy("org.jetbrains.kotlin:kotlin-stdlib")
            }
            module("org.jetbrains.kotlin:kotlin-stdlib-jdk8") {
                replacedBy("org.jetbrains.kotlin:kotlin-stdlib")
            }
        }
    }
}
allprojects {
    kotlinDependenciesConfig()
}