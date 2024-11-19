package test

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.datastore.preferences.core.longPreferencesKey
import com.crow.mordecaix.common.TodoEntity
import com.crow.mordecaix.common.getAppDatabase
import com.crow.mordecaix.extensions.asyncEncode

@Composable
fun TestApp() {
    LaunchedEffect(Unit) {
        println(getAppDatabase().getDao().insert(TodoEntity(title = "123", content = "456")))
        longPreferencesKey("Key").asyncEncode(123L)
    }
    Text("HELLO, WORLD!")
}