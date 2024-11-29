package com.crow.mordecaix

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.crow.mordecaix.common.getAppDatabase
import com.crow.mordecaix.ui.theme.BLUEF9
import com.crow.mordecaix.ui.theme.MordecaiXTheme
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
import java.awt.Dimension

@OptIn(ExperimentalResourceApi::class)
fun main() = application {
    initializeApplication()
    getAppDatabase()
    Window(
        onCloseRequest = ::exitApplication,
        title = "MordecaiX",
    ) {
        window.minimumSize = Dimension(352,320)

        App()
    }
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
                title = "MordecaiX",
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