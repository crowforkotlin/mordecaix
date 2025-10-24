package com.crow.mordecaix

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import com.crow.mordecaix.common.getAppDatabase
import com.crow.mordecaix.ui.theme.BLUEF9
import com.crow.mordecaix.ui.theme.MordecaiXTheme
import com.crow.mordecaix.zipline.Host
import io.ktor.http.HttpHeaders.Host
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import org.jetbrains.jewel.intui.standalone.theme.default
import org.jetbrains.jewel.intui.standalone.theme.lightThemeDefinition
import org.jetbrains.jewel.intui.window.decoratedWindow
import org.jetbrains.jewel.intui.window.styling.light
import org.jetbrains.jewel.intui.window.styling.lightWithLightHeader
import org.jetbrains.jewel.ui.ComponentStyling
import org.jetbrains.jewel.window.DecoratedWindow
import org.jetbrains.jewel.window.DecoratedWindowScope
import org.jetbrains.jewel.window.TitleBar
import org.jetbrains.jewel.window.styling.TitleBarColors
import org.jetbrains.jewel.window.styling.TitleBarStyle
import org.koin.compose.getKoin
import org.koin.core.Koin
import java.awt.Dimension

@OptIn(ExperimentalResourceApi::class)
fun main() = application {
    initializeApplication()
    getAppDatabase()
    Host(CoroutineScope(Dispatchers.Default)).start()
    IntUiTheme(
        theme = JewelTheme.lightThemeDefinition(),
        styling = ComponentStyling.default()
            .decoratedWindow(
                titleBarStyle = TitleBarStyle.light(
                    colors = TitleBarColors.lightWithLightHeader(backgroundColor = BLUEF9)
                )
            ),
    ) {
        MordecaiXTheme {
            DecoratedWindow(
                onCloseRequest = ::exitApplication,
                title = "mordecaix ",
                enabled = true,
                visible = true,
                icon = BitmapPainter(useResource("favicon.ico") {
                    it.readBytes().decodeToImageBitmap()
                })
            ) {
                window.minimumSize = Dimension(352, 320)
                DesktopTitleBar()
                DesktopApp()
            }
        }
    }
}
@Composable
fun LazyColumnWithConstrainedBar() {
    var selectedIndex by remember { mutableStateOf(0) }

    // 动态存储每个 Item 的 Y 偏移量
    val itemOffsets = remember { mutableStateMapOf<Int, Float>() }

    // 滚动状态
    val listState = rememberLazyListState()
    LaunchedEffect(listState.layoutInfo) {
        // 获取所有项的偏移量信息
        listState.layoutInfo.visibleItemsInfo.forEach { itemInfo ->
            val itemIndex = itemInfo.index
            val itemOffset = itemInfo.offset

            // 如果是目标项，可以计算相对于第一个项（index = 0）的偏移量
            if (itemIndex > 0) {
                var relativeOffset = itemOffset
                // 累加前面所有项的高度，直到目标项
                for (i in 0 until itemIndex) {
                    listState.layoutInfo.visibleItemsInfo.find { it.index == i }?.let {
                        relativeOffset += it.offset
                    }
                }
                println("Offset of item $itemIndex relative to item 0: $relativeOffset")
            }
        }
    }
    // 蓝条的 Y 偏移量
    val barOffsetY by animateDpAsState(
        targetValue = run {
            val itemOffset = itemOffsets[selectedIndex] ?: 0f
            val columnOffset = listState.layoutInfo.viewportStartOffset.toFloat()
            val constrainedOffset = itemOffset.coerceIn(columnOffset, columnOffset + listState.layoutInfo.viewportEndOffset)
            constrainedOffset.dp
        },
        animationSpec = tween(
            durationMillis = 100,
            easing = LinearEasing
        ),
    )



    val items = List(20) { "Item $it" }

    Box(modifier = Modifier.fillMaxSize()) {
        // 蓝条
        Box(
            modifier = Modifier
                .width(5.dp)
                .height(50.dp) // 蓝条高度可以动态调整
                .offset(y = barOffsetY)
                .background(Color.Blue)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState // 绑定滚动状态
        ) {
            itemsIndexed(items) { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(10.dp)
                        .clickable { selectedIndex = index }
                ) {
                    Text(
                        text = item,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun DecoratedWindowScope.DesktopTitleBar(
    title: String = "MordecaiX"
) {
    TitleBar {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
    }
}