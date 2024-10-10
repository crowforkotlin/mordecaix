package com.crow.mordecaix.model.di

import com.crow.mordecaix.model.exposed.SourceModel
import org.koin.dsl.module

val extensionModule = module {
    single {
        listOf(
            SourceModel(
                mName = "拷贝漫画",
                mAuthor = "crowforkotlin",
                mVersion = "1.0.0",
                mLanguage = "ZH-CN",
                mIncludeNsfw = true,
                mReaderType = "Comic"
            ),
            SourceModel(
                mName = "热辣漫画",
                mAuthor = "crowforkotlin",
                mVersion = "1.0.0",
                mLanguage = "ZH-CN",
                mIncludeNsfw = true,
                mReaderType = "Comic"
            )
        )
    }
}