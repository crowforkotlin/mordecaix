import java.io.File
import java.io.FileInputStream

// 接收路径参数
val pluginDataStoreInputDirPath: String by project
val pluginDataStoreOutputDirPath: String by project

// 创建任务
tasks.register("plugin.DecodePreferencesPb") {
    val dir = File(pluginDataStoreOutputDirPath)
    val inputDir = File(pluginDataStoreInputDirPath)
    if (!dir.exists()) dir.mkdir()

    inputDir.listFiles()?.forEach { file ->
        if (file.name.endsWith(".preferences_pb")) {
            val command = listOf("protoc", "--decode_raw")
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
