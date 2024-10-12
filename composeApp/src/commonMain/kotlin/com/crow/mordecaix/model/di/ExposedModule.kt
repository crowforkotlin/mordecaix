package com.crow.mordecaix.model.di

import com.crow.mordecaix.model.exposed.SourceModel
import org.koin.dsl.module

val extensionModule = module {
    single {
        listOf(
            SourceModel(
                mName = "testccccc",
                mAuthor = "crowforkotlin",
                mVersion = "1.0.0",
                mLanguage = "ZH-CN",
                mIncludeNsfw = true,
                mReaderType = "Comic"
            ),
            SourceModel(
                mName = "testAA",
                mAuthor = "crowforkotlin",
                mVersion = "1.0.0",
                mLanguage = "ZH-CN",
                mIncludeNsfw = true,
                mReaderType = "Comic"
            ),
            SourceModel(
                mName = "testccccc",
                mAuthor = "crowforkotlin",
                mVersion = "1.0.0",
                mLanguage = "ZH-CN",
                mIncludeNsfw = true,
                mReaderType = "Comic"
            ),
            SourceModel(
                mName = "testAA",
                mAuthor = "crowforkotlin",
                mVersion = "1.0.0",
                mLanguage = "ZH-CN",
                mIncludeNsfw = true,
                mReaderType = "Comic"
            ), SourceModel(
                mName = "testccccc",
                mAuthor = "crowforkotlin",
                mVersion = "1.0.0",
                mLanguage = "ZH-CN",
                mIncludeNsfw = true,
                mReaderType = "Comic"
            ),
            SourceModel(
                mName = "testAA",
                mAuthor = "crowforkotlin",
                mVersion = "1.0.0",
                mLanguage = "ZH-CN",
                mIncludeNsfw = true,
                mReaderType = "Comic"
            )
        )
    }
}