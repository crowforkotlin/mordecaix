package com.crow.mordecaix.zipline.services

import app.cash.zipline.ZiplineService
import com.crow.mordecaix.zipline.model.Plugin

interface Application : BaseServices     { fun getPlugins() : List<Plugin> }
