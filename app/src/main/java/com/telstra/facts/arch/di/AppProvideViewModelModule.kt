package com.telstra.facts.arch.di

import androidx.lifecycle.ViewModel
import com.telstra.facts.viewmodel.FactsListViewModel
import com.telstra.facts.viewmodel.FactsUseCase
import com.telstra.facts.arch.viewmodel.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class AppProvideViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(FactsListViewModel::class)
    internal fun provideFactsListViewModel(factsUseCase: FactsUseCase): ViewModel =
        FactsListViewModel(factsUseCase)
}
