package com.crow.mordecaix

import androidx.compose.runtime.Composable
import com.crow.mordecaix.ui.screen.MordecaiXApp
import com.crow.mordecaix.ui.theme.MordecaiXTheme
import test.sample.Samples


@Composable
fun App() { MordecaiXTheme { MordecaiXApp() } }

@Composable
fun DesktopApp() { MordecaiXApp() }

@Composable
fun TestApp() { MordecaiXTheme { TestApp()  } }

@Composable
fun HazeSampleApp() { Samples("Haze Sample") }