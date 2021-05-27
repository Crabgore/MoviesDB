package com.crabgore.moviesDB.di.modules

import androidx.lifecycle.ViewModel
import com.crabgore.moviesDB.di.ViewModelBuilder
import com.crabgore.moviesDB.di.ViewModelKey
import com.crabgore.moviesDB.ui.people.PeopleFragment
import com.crabgore.moviesDB.ui.people.PeopleViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class PeopleFragmentModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun peopleFragment(): PeopleFragment

    @Binds
    @IntoMap
    @ViewModelKey(PeopleViewModel::class)
    abstract fun bindViewModel(viewModel: PeopleViewModel): ViewModel
}