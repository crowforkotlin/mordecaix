package com.crow.mordecaix.model.di

import com.crow.mordecaix.ui.viewmodel.AppViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewmodelModule = module {
    viewModel { AppViewModel() }
}

