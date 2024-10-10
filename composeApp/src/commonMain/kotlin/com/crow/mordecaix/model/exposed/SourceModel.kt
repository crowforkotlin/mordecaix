package com.crow.mordecaix.model.exposed

data class SourceModel(
    val mName: String,
    val mAuthor: String,
    val mVersion: String,
    val mLanguage: String,
    val mIncludeNsfw: Boolean,
    val mReaderType: String
)
