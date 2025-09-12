@file:Suppress("SetTextI18n", "SpellCheckingInspection")

package mordecaix

import android.app.Activity
import android.os.Bundle
import com.crow.test.android.databinding.ActivityMainBinding

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
├── android-compose/
└── (current) android/
""".trimIndent()
    }
}