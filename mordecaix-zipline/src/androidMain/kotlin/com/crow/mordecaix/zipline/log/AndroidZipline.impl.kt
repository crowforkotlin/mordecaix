/*
 * Copyright (C) 2022 Block, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.crow.mordecaix.zipline.log

import android.content.Context
import app.cash.zipline.loader.ManifestVerifier.Companion.NO_SIGNATURE_CHECKS
import app.cash.zipline.loader.ZiplineLoader
import com.crow.mordecaix.zipline.startHostZipline
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import okhttp3.OkHttpClient
import java.util.concurrent.Executors


class AndroidZipline(
  private val applicationContext: Context,
  private val scope: CoroutineScope,
) {
  private val ziplineExecutorService = Executors.newSingleThreadExecutor { Thread(it, "Zipline") }
  private val ziplineDispatcher = ziplineExecutorService.asCoroutineDispatcher()
  private val okHttpClient = OkHttpClient()

  fun start() {
    startHostZipline(
      scope = scope,
      ziplineDispatcher = ziplineDispatcher,
      ziplineLoader = ZiplineLoader(
        dispatcher = ziplineDispatcher,
        manifestVerifier = NO_SIGNATURE_CHECKS,
        httpClient = okHttpClient,
      ),
      manifestUrl = "http://192.168.137.1:8080/manifest.zipline.json",
      host = AndroidLogger()
    )
  }

  fun close() {
    ziplineExecutorService.shutdown()
  }

}