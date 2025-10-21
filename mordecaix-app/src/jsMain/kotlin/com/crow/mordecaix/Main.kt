package com.crow.mordecaix

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.crow.mordecaix.js.Database
import com.crow.mordecaix.js.IDB
import com.crow.mordecaix.js.TransactionMode
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.skiko.wasm.onWasmReady
import kotlin.js.json

@OptIn(ExperimentalComposeUiApi::class)
fun main() = onWasmReady {
    initializeApplication()
    val scope = CoroutineScope(Dispatchers.Default)
    scope.launch { idb() }
    ComposeViewport(document.body!!) {
        App()
    }
}

suspend fun idb() = coroutineScope {
    val db = Database( IDB.openDB("myApp", 1))


    // 添加数据
    db.put("users", json(
        "id" to 1,
        "name" to "张三",
        "email" to "zhang@example.com"
    ))

    // 查询数据
    val user = db.get<dynamic>("users", 1)
    println(user?.name)

    // 使用事务
    val tx = db.transaction("users", TransactionMode.READWRITE)
    val store = tx.objectStore("users")
    store.delete(1)
    tx.await()

    db.close()
}