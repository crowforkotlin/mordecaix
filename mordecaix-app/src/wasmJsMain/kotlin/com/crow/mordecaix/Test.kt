package com.crow.mordecaix

import com.crow.mordecaix.extensions.console
import kotlinx.coroutines.await
import kotlin.js.Promise

@JsName("AI")
external fun AI(): Promise<AIModuleInstance>

external interface AIModuleInstance : JsAny {

    @JsName("ccall")
    fun function(
        ident: String,
        returnType: String,
        argTypes: JsArray<JsString>,
        args: JsArray<JsAny?>
    ): JsAny?

    @JsName("onRuntimeInitialized")
    var onRuntimeInitialized: (() -> Unit)?
}

// 这是一个 suspend 函数，用于异步加载 WASM 模块并调用其中的函数
suspend fun loadAndCallWasm() {
    console.log("Starting WASM module loading process from Kotlin...");

    try {
        // 调用外部的 AI() 工厂函数，它返回一个 Promise
        // 使用 .await() 等待 Promise 完成，获取模块实例
        // 这里的 AI() 调用会映射到 JavaScript 中的全局 AI() 调用
        val moduleInstance = AI().await<AIModuleInstance>()

        console.log("WASM module loaded successfully from Kotlin: $moduleInstance");


        try {
            val result = moduleInstance.function(
                ident = "HttpTest",
                returnType = "number",
                argTypes = JsArray<JsString>().apply { set(0, "string".toJsString()) },
                args = JsArray<JsAny?>().apply { set(0, "https://jsonplaceholder.typicode.com/posts/1".toJsString()) },
            )
            console.log("Result from WASM Add function called from Kotlin: 5 + 3 = $result"); // 将 JsNumber 转换为 Int

        } catch (error: Throwable) {
            console.error("Error calling Add function from Kotlin:  ${error.message}");
        }

    } catch (error: Throwable) {
        console.error("WASM module failed to load from Kotlin: ${error.message}");
    }
}