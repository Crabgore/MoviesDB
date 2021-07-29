package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.ui.tv.details.TVDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val tvDetailsModule =
    module {
        viewModel { TVDetailsViewModel(get(), get(), get()) }
    }