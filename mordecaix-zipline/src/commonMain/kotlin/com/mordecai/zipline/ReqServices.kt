package com.mordecai.zipline

import app.cash.zipline.Zipline
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
class ReqServices  : ReqService {
    override fun get(url: String) {
        CoroutineScope(Dispatchers.Default).launch {
            val response = HttpClient().get(url)
            println(response.bodyAsText())
        }
    }
}*/
