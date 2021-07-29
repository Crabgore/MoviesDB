package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule =
    module {
        viewModel { SearchViewModel(get()) }
    }