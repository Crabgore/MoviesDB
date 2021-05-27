package com.crabgore.moviesDB.di.modules

import androidx.lifecycle.ViewModel
import com.crabgore.moviesDB.di.ViewModelBuilder
import com.crabgore.moviesDB.di.ViewModelKey
import com.crabgore.moviesDB.ui.movie.category.MoviesCategoryFragment
import com.crabgore.moviesDB.ui.movie.category.MoviesCategoryViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class MoviesCategoryModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun moviesCategoryFragment(): MoviesCategoryFragment

    @Binds
    @IntoMap
    @ViewModelKey(MoviesCategoryViewModel::class)
    abstract fun bindViewModel(viewModel: MoviesCategoryViewModel): ViewModel
}