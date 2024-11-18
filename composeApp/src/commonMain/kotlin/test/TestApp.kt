package test

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.crow.mordecaix.common.TodoEntity
import com.crow.mordecaix.common.getAppDatabase

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TestApp() {
    LaunchedEffect(Unit) {
        println(getAppDatabase().getDao().insert(TodoEntity(title = "123", content = "456")))
    }
}