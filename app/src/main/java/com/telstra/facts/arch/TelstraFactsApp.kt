package com.telstra.facts.arch

import android.util.Log
import com.telstra.facts.arch.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import java.io.IOException
import java.net.SocketException


class TelstraFactsApp : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()

        setupRxErrorHandler()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val context = applicationContext

        return DaggerApplicationComponent
            .builder()
            .application(this)
            .networkModule(NetworkModule(context))
            .build()
    }

    private fun setupRxErrorHandler() {
        RxJavaPlugins.setErrorHandler { ex ->
            when (val e = if (ex is UndeliverableException) ex.cause else ex) {
                is IOException,
                is SocketException -> {
                    return@setErrorHandler
                }
                is InterruptedException -> {
                    return@setErrorHandler
                }
                is NullPointerException,
                is IllegalArgumentException -> {
                    Thread.currentThread()
                        .uncaughtExceptionHandler
                        ?.uncaughtException(Thread.currentThread(), e)
                    return@setErrorHandler
                }
                is IllegalStateException -> {
                    Thread.currentThread()
                        .uncaughtExceptionHandler
                        ?.uncaughtException(Thread.currentThread(), e)
                    return@setErrorHandler
                }
                else -> Log.w(
                    "Error",
                    "RX ERROR HANDLER: Undeliverable exception received, not sure what to do: $e"
                )
            }
        }
    }
}
