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
//            App()
            MaterialTheme {
                WasmFibApp()
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


@Composable
fun WasmFibApp() {
    val context = LocalContext.current
    val nativeLib = remember { NativeLib(context) }
    val coroutineScope = rememberCoroutineScope()
    var resultText by remember { mutableStateOf("Press the button") }
    val index = 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = resultText)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            resultText = "Running"
            coroutineScope.launch(Dispatchers.IO) {
                val startMs = System.currentTimeMillis()
                val result = nativeLib.nativeWasmFib(nativeLib.fibonacciWasmImageBytes, index)
                val endMs = System.currentTimeMillis()
                val duration = endMs - startMs
                withContext(Dispatchers.Main) { resultText = "fib($index) -> $result, $duration ms \t ${duration / 1000} s" }
            }
        }) {
            Text("Run Fib")
        }
    }
}

