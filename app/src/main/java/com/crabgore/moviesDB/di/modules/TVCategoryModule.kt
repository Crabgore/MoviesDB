package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.ui.tv.category.TVCategoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val tvCategoryModule =
    module {
        viewModel { TVCategoryViewModel(get(), get(), get()) }
    }