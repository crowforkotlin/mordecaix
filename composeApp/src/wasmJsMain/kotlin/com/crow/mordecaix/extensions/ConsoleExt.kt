package com.crow.mordecaix.extensions

@JsName("Console")
internal open external class JsConsole {
    fun error(vararg msg: String)
    fun warn(vararg msg: String)
    fun info(vararg msg: String)
    fun log(vararg msg: String)
}

internal external val console: JsConsole /* compiled code */

