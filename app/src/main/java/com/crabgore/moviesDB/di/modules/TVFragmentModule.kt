package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.ui.tv.TVViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val tvFragmentModule =
    module {
        viewModel { TVViewModel(get()) }
    }