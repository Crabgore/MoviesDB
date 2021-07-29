package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.ui.user.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val userModule =
    module {
        viewModel { UserViewModel(get(), get(), get(), get()) }
    }