package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.ui.movie.category.MoviesCategoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moviesCategoryModule =
    module {
        viewModel { MoviesCategoryViewModel(get(), get(), get()) }
    }