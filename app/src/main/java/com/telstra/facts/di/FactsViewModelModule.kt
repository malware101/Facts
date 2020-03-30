package com.telstra.facts.di

import androidx.lifecycle.ViewModel
import com.telstra.facts.viewmodel.FactsListViewModel
import com.telstra.facts.arch.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FactsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FactsListViewModel::class)
    abstract fun bindFactsListViewModel(viewModel: FactsListViewModel): ViewModel

}
