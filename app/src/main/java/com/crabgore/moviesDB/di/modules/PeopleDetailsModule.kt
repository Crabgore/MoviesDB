package com.crabgore.moviesDB.di.modules

import androidx.lifecycle.ViewModel
import com.crabgore.moviesDB.di.ViewModelBuilder
import com.crabgore.moviesDB.di.ViewModelKey
import com.crabgore.moviesDB.ui.people.details.PeopleDetailsFragment
import com.crabgore.moviesDB.ui.people.details.PeopleDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class PeopleDetailsModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun peopleDetailsFragment(): PeopleDetailsFragment

    @Binds
    @IntoMap
    @ViewModelKey(PeopleDetailsViewModel::class)
    abstract fun bindViewModel(viewModel: PeopleDetailsViewModel): ViewModel
}