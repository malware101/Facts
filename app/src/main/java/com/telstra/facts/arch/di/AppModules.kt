package com.telstra.facts.arch.di

import com.telstra.facts.arch.NetworkModule
import com.telstra.facts.di.FactsBindingModule
import dagger.Module

@Module(
    includes = [
        NetworkModule::class,
        DesignSingletonModule::class
    ]
)
class AppModules
