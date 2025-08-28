package com.crow.mordecaix.model.di

import io.ktor.client.HttpClient
import org.koin.dsl.module

val networkModule = module {
    single { HttpClient() }
}