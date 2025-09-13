@file:Suppress("SpellCheckingInspection")

package mordecaix

import android.app.Activity
import android.os.Bundle
import com.crow.test.compose.databinding.ActivityMainBinding

class MainActivity : Activity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.content.text = formateStruct()
    }

    private fun formateStruct(): String {
        return """
mordecaix-test/
├── (current) android-compose/
└── android/
""".trimIndent()
    }
}