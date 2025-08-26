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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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