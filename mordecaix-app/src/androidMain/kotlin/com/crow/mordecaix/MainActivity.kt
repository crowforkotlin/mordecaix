package com.crow.mordecaix

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets


class MainActivity : ComponentActivity() {
    private val TAG = "WasmExecutor"

    @SuppressLint("RequiresFeature")
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.navigationBarColor = Color.TRANSPARENT
        }
        setContent {
            MaterialTheme {
                App()
            }
        }
    }

    @Preview
    @Composable
    fun AppAndroidPreview() {
        App()
    }


    private suspend fun readAssetFile(context: Context, fileName: String): ByteArray? =
        withContext(Dispatchers.IO) {
            val assetManager: AssetManager = context.assets
            try {
                assetManager.open(fileName).use { `is` ->
                    ByteArrayOutputStream().use { buffer ->
                        val data = ByteArray(1024)
                        var nRead: Int
                        while (`is`.read(data, 0, data.size).also { nRead = it } != -1) {
                            buffer.write(data, 0, nRead)
                        }
                        buffer.flush()
                        buffer.toByteArray()
                    }
                }
            } catch (e: IOException) {
                null
            }
        }

    private suspend fun readAssetTextFile(context: Context, fileName: String): String? {
        val bytes = readAssetFile(context, fileName)
        val a = bytes?.toString(StandardCharsets.UTF_8)
        return a
    }
}