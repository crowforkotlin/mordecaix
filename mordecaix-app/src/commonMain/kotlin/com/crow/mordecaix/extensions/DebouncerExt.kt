@file:OptIn(ExperimentalTime::class)

package com.crow.mordecaix.extensions

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlinx.datetime.Clock
import kotlin.time.ExperimentalTime

val LocalDebounce = compositionLocalOf { 0L }



/**
 * Wraps an [onClick] lambda with another one that supports debouncing. The default deboucing time
 * is 1000ms.
 *
 * @return debounced onClick
 */
@Composable
inline fun debounced(crossinline onClick: () -> Unit, debounceTime: Long = 1000L): () -> Unit {
    var lastTimeClicked by remember { mutableStateOf(0L) }
    val onClickLambda: () -> Unit = {

        val now = kotlin.time.Clock.System.now().toEpochMilliseconds()
        if (now - lastTimeClicked > debounceTime) {
            onClick()
        }
        lastTimeClicked = now
    }
    return onClickLambda
}

/**
 * The same as [Modifier.clickable] with support to debouncing.
 */
fun Modifier.debouncedClickable(
    debounceTime: Long = 1000L,
    onClick: () -> Unit
): Modifier {
    return this.composed {
        val clickable = debounced(debounceTime = debounceTime, onClick = { onClick() })
        this.clickable { clickable() }
    }
}

@Composable
fun onDebounceClick(
    debounceTimeMillis: Long = 1000L,
    onClick: () -> Unit,
): () -> Unit {
    var lastClickTimeMillis: Long by remember { mutableLongStateOf(value = 0L) }
    return {
        kotlin.time.Clock.System.now().toEpochMilliseconds().let { currentTimeMillis ->
            if ((currentTimeMillis - lastClickTimeMillis) >= debounceTimeMillis) {
                lastClickTimeMillis = currentTimeMillis
                onClick()
            }
        }
    }
}