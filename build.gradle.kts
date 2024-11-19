import java.io.FileInputStream

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.ksp) apply false
}



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

