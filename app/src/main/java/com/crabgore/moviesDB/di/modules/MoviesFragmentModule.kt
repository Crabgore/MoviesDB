package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.ui.movie.MoviesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moviesFragmentModule =
    module {
        viewModel { MoviesViewModel(get()) }
    }