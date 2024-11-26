package com.crow.mordecaix

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.crow.mordecaix.common.getAppDatabase
import org.jetbrains.compose.resources.ExperimentalResourceApi
import java.awt.Dimension

/*import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.jewel.foundation.BorderColors
import org.jetbrains.jewel.foundation.GlobalColors
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import org.jetbrains.jewel.intui.standalone.theme.createDefaultTextStyle
import org.jetbrains.jewel.intui.standalone.theme.createEditorTextStyle
import org.jetbrains.jewel.intui.standalone.theme.darkThemeDefinition
import org.jetbrains.jewel.intui.standalone.theme.default
import org.jetbrains.jewel.intui.standalone.theme.light
import org.jetbrains.jewel.intui.standalone.theme.lightThemeDefinition
import org.jetbrains.jewel.intui.window.decoratedWindow
import org.jetbrains.jewel.intui.window.styling.light
import org.jetbrains.jewel.ui.ComponentStyling
import org.jetbrains.jewel.window.DecoratedWindow
import org.jetbrains.jewel.window.styling.DecoratedWindowColors
import org.jetbrains.jewel.window.styling.DecoratedWindowStyle
import org.jetbrains.jewel.window.styling.TitleBarColors
import org.jetbrains.jewel.window.styling.TitleBarStyle*/

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalResourceApi::class)
fun main() = application {
    initializeApplication()
    val state = rememberWindowState()
    getAppDatabase()
    Window(
        state = state,
        onCloseRequest = ::exitApplication,
        title = "MordecaiX",
    ) {
        with(LocalDensity.current) {
            window.minimumSize = Dimension(352,320)
        }
        App()
    }
}
        /*
        fun main() = SwingUtilities.invokeLater {
            initializeApplication()
            val window = JFrame()
            window.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
            window.title = "MordecaiX"
            window.minimumSize = Dimension(352,320)
            val composePanel = ComposePanel()
            getAppDatabase()

            composePanel.setContent {
                window.rootPane.putClientProperty("JRootPane.titleBarBackground", ColorUIResource(MaterialTheme.colorScheme.surface.toArgb()))
                App()
            }
            JFrame.setDefaultLookAndFeelDecorated(true)
            window.setSize(1000, 600)
            window.setLocationRelativeTo(null)
            window.isVisible = true
            window.contentPane = composePanel
        }*/

/*fun main() = application {
    initializeApplication()
    getAppDatabase()
    val textStyle = JewelTheme.createDefaultTextStyle()

    val editorStyle = JewelTheme.createEditorTextStyle()


    val themeDefinition =
        JewelTheme.lightThemeDefinition(defaultTextStyle = textStyle, editorTextStyle = editorStyle.copy(color = Color.Yellow),
            colors = GlobalColors.light(borders = BorderColors.light(
                normal = Color.Green
        ))
        )
    IntUiTheme(
        theme = JewelTheme.darkThemeDefinition(),
        styling =

        ComponentStyling.default()
            .decoratedWindow(
                windowStyle = DecoratedWindowStyle.light(colors = DecoratedWindowColors.light(borderColor = Color.Green, inactiveBorderColor = Color.Green)),
                titleBarStyle = TitleBarStyle.light(
                    colors = TitleBarColors.light(
                        backgroundColor = Color.Green,
                        borderColor = Color.Red,
                        inactiveBackground = Color.Red

                    )
                )
            ),
    ) {
        DecoratedWindow(
            onCloseRequest = ::exitApplication,
            title = "MordecaiX2222",
            visible = true,
            icon = BitmapPainter(useResource("favicon.ico") {
                it.readBytes().decodeToImageBitmap()
            })
        ) {
//            window.minimumSize = Dimension(352,320)
            App()
        }
    }
}*/
