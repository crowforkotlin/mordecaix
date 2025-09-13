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
package com.mordecai.zipline

import android.system.Os.bind
import android.util.Log
import app.cash.zipline.Zipline
import app.cash.zipline.loader.DefaultFreshnessCheckerNotFresh
import app.cash.zipline.loader.LoadResult
import app.cash.zipline.loader.ZiplineLoader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.serialization.modules.EmptySerializersModule

fun startHostZipline(
  scope: CoroutineScope,
  ziplineDispatcher: CoroutineDispatcher,
  ziplineLoader: ZiplineLoader,
  manifestUrl: String,
  host: LogService
) {
  scope.launch(ziplineDispatcher + SupervisorJob()) {
    ziplineLoader.loadOnce(
      applicationName = "",
      freshnessChecker = DefaultFreshnessCheckerNotFresh,
      manifestUrl = manifestUrl,
      serializersModule = EmptySerializersModule(),
      initializer = { it.bind<LogService>("androidLog", host) }
    )
      .apply {
        val result = this
        if (result is LoadResult.Success) {
          val zipline = result.zipline
          Log.d("AndroidLogger", "zipline result is $zipline")
          val log = zipline.take<LogService>("JsLog")
          log.log("hello from android!")
          Log.d("AndroidLogger", "zipline result is $result")
        }
      }
    return@launch
    val loadResultFlow: Flow<LoadResult> = ziplineLoader.load(
      applicationName = "Logger",
      freshnessChecker = DefaultFreshnessCheckerNotFresh,
      manifestUrlFlow = flow(manifestUrl, 500L),
      initializer = { zipline: Zipline ->
//        zipline.bind<ReqService>("request", ReqServices())
        zipline.bind<LogService>("androidLog", host)
      },
    )
    loadResultFlow.collect { result ->
      if (result is LoadResult.Success) {
        val zipline = result.zipline
        Log.d("AndroidLogger", "zipline result is $zipline")
        val log = zipline.take<LogService>("JsLog")
        log.log("hello from android!")
        Log.d("AndroidLogger", "zipline result is $result")
      }
    }
  }
}

private fun <T> repeatFlow(content: T, delayMillis: Long): Flow<T> {
  return flow {
    while (true) {
      emit(content)
      delay(delayMillis)
    }
  }
}

private fun <T> flow(content: T, delayMillis: Long): Flow<T> {
  return flow { emit(content) }
}
