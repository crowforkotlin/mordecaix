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

import app.cash.zipline.Zipline
import app.cash.zipline.loader.DefaultFreshnessCheckerNotFresh
import app.cash.zipline.loader.LoadResult
import app.cash.zipline.loader.ZiplineLoader
import com.crow.mordecaix.zipline.services.BaseLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.serialization.modules.EmptySerializersModule

abstract class BaseHost(
    private val scope: CoroutineScope
) {

    private val hostJob = SupervisorJob()
    private val ziplineFlow = MutableSharedFlow<LoadResult>()

    fun loadOnce(
        ziplineDispatcher: CoroutineDispatcher,
        ziplineLoader: ZiplineLoader,
        manifestUrl: String,
        initializer: suspend (Zipline) -> Unit
    ) {
        scope.launch(context = ziplineDispatcher + hostJob) {
            ziplineFlow.emit(
                value = ziplineLoader.loadOnce(
                    applicationName = "",
                    freshnessChecker = DefaultFreshnessCheckerNotFresh,
                    manifestUrl = manifestUrl,
                    serializersModule = EmptySerializersModule(),
                    initializer = initializer
                )
            )
        }
    }

    fun load(
        ziplineDispatcher: CoroutineDispatcher,
        ziplineLoader: ZiplineLoader,
        manifestUrl: String,
        initializer: suspend (Zipline) -> Unit
    ) {
        scope.launch(context = ziplineDispatcher + hostJob) {
            ziplineFlow.emit(
                value = ziplineLoader.load(
                    applicationName = "",
                    freshnessChecker = DefaultFreshnessCheckerNotFresh,
                    manifestUrl = manifestUrl,
                    serializersModule = EmptySerializersModule(),
                    initializer = initializer
                )
            )
        }
    }

    fun <T> repeatFlow(content: T, delayMillis: Long): Flow<T> {
        return flow {
            while (true) {
                emit(content)
                delay(delayMillis)
            }
        }
    }

    fun <T> flow(content: T, delayMillis: Long): Flow<T> {
        return flow { emit(content) }
    }

}