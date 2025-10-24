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
package com.crow.mordecaix.zipline

import app.cash.zipline.loader.ManifestVerifier.Companion.NO_SIGNATURE_CHECKS
import app.cash.zipline.loader.ZiplineHttpClient
import app.cash.zipline.loader.ZiplineLoader
import com.crow.mordecaix.zipline.impl.Logger
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import okio.ByteString
import okio.ByteString.Companion.toByteString
import org.koin.mp.KoinPlatform.getKoin
import java.util.concurrent.Executors


class Host(
  private val scope: CoroutineScope,
) : BaseHost(scope) {

  private val ziplineExecutorService = Executors.newSingleThreadExecutor { Thread(it, "Zipline") }
  private val ziplineDispatcher = ziplineExecutorService.asCoroutineDispatcher()
  private val koin by lazy { getKoin() }

  fun start() {
    val httpClient = koin.get<HttpClient>()
    load(
      ziplineDispatcher = ziplineDispatcher,
      ziplineLoader = ZiplineLoader(
        dispatcher = ziplineDispatcher,
        manifestVerifier = NO_SIGNATURE_CHECKS,
        httpClient = object : ZiplineHttpClient() {
          override suspend fun download(url: String, requestHeaders: List<Pair<String, String>>): ByteString {
            return httpClient.get(urlString = url) { requestHeaders.forEach { header -> headers[header.first] = header.second } }.readRawBytes().toByteString()
          }
        },
      ),
      manifestUrl = "http://192.168.137.1:8000/manifest.zipline.json",
      initializer = { zipline ->
        zipline.bind(name = "Logger", instance = Logger())
      }
    )
  }

  fun close() {
    ziplineExecutorService.shutdown()

  }

}