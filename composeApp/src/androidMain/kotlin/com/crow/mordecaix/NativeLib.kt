package com.crow.mordecaix

import android.content.Context

class NativeLib(ctx : Context) {

    companion object {
        init {
            System.loadLibrary("wasmedge_android_app")
        }
    }

    external fun stringFromJNI(): String?

    external fun nativeWasmFib(wasm_bytes: ByteArray?, idx: Int): Int

    var fibonacciWasmImageBytes : ByteArray = ctx.assets.open("fibonacci.wasm").readBytes()


}
