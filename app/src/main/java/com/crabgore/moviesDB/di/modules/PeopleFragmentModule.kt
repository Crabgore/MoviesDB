package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.ui.people.PeopleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val peopleFragmentModule =
    module {
        viewModel { PeopleViewModel(get()) }
    }