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
  host: BaseLogger
) {
  scope.launch(ziplineDispatcher + SupervisorJob()) {
    ziplineLoader.loadOnce(
      applicationName = "",
      freshnessChecker = DefaultFreshnessCheckerNotFresh,
      manifestUrl = manifestUrl,
      serializersModule = EmptySerializersModule(),
      initializer = {
        repeat(10000) { count ->
          it.bind<BaseLogger>(name = "logger$count", host)
        }
      }
    )
      .apply {
        val result = this
        println(result)
        if (result is LoadResult.Success) {
          val zipline = result.zipline
          val log = zipline.take<BaseLogger>(name = "JsLog")
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
