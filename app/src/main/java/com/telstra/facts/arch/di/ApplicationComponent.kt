package com.telstra.facts.arch.di

import android.app.Application
import com.telstra.facts.arch.NetworkModule
import com.telstra.facts.arch.TelstraFactsApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityBindingModule::class,
        ViewModelBindingModule::class,
        AppModules::class
    ]
)
interface ApplicationComponent : AndroidInjector<TelstraFactsApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): ApplicationComponent.Builder
        fun networkModule(network: NetworkModule): ApplicationComponent.Builder
        fun build(): ApplicationComponent
    }
}
