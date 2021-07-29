package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.ui.movie.details.MovieDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val movieDetailsModule =
    module {
        viewModel { MovieDetailsViewModel(get(), get(), get()) }
    }