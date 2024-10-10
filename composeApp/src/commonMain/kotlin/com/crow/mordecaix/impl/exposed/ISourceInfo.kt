package com.crow.mordecaix.impl.exposed

import com.crow.mordecaix.model.exposed.SourceModel

interface ISourceInfo {
    val mSources: MutableList<SourceModel>
}