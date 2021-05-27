package com.crabgore.moviesDB.di.modules

import androidx.lifecycle.ViewModel
import com.crabgore.moviesDB.di.ViewModelBuilder
import com.crabgore.moviesDB.di.ViewModelKey
import com.crabgore.moviesDB.ui.tv.category.TVCategoryViewModel
import com.crabgore.moviesDB.ui.tv.category.TVCategoryFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class TVCategoryModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun tvCategoryFragment(): TVCategoryFragment

    @Binds
    @IntoMap
    @ViewModelKey(TVCategoryViewModel::class)
    abstract fun bindViewModel(viewModel: TVCategoryViewModel): ViewModel
}