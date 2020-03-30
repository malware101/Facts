package com.telstra.facts.arch.di

import com.telstra.facts.di.FactsBindingModule
import com.telstra.facts.ui.FactsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(
        modules = [
            FactsBindingModule::class
        ]
    )
    abstract fun bindFactsActivity(): FactsActivity
}
