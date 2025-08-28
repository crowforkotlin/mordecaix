// plugin.datastore.gradle.kts
import java.io.File
import java.io.FileInputStream

val pluginDataStoreInputDirPath: String by project
val pluginDataStoreOutputDirPath: String by project

tasks.register("plugin.DecodePreferencesPb") {
    group = "Custom Tasks" // 给任务分组，便于查找
    description = "Decodes .preferences_pb files from DataStore."

    // 将输入和输出声明为任务的属性，这对于缓存和任务依赖分析至关重要
    val inputDir = project.file(pluginDataStoreInputDirPath)
    val outputDir = project.file(pluginDataStoreOutputDirPath)
    inputs.dir(inputDir)
    outputs.dir(outputDir)

    // 使用 doLast 将所有操作放入执行阶段
    doLast {
        if (!outputDir.exists()) {
            outputDir.mkdir()
        }

        inputDir.listFiles()?.forEach { file ->
            if (file.name.endsWith(".preferences_pb")) {
                println("Processing ${file.name}...") // 增加日志，方便调试
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
                    throw GradleException("protoc command failed with exit code $exitCode for file ${file.path}")
                }
                val outputFile = File(outputDir, "${file.nameWithoutExtension}_datastore.txt")
                outputFile.writeText(result)
                println("Successfully decoded to ${outputFile.path}")
            }
        }
    }
}