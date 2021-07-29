package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.ui.people.details.PeopleDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val peopleDetailsModule =
    module {
        viewModel { PeopleDetailsViewModel(get()) }
    }