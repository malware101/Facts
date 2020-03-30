package com.telstra.facts.arch

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    companion object {
        const val ANONYMOUS = "ANONYMOUS"

        private const val CONNECT_TIMEOUT = 30L
        private const val READ_TIMEOUT = 30L
        private const val WRITE_TIMEOUT = 30L
    }

    @Provides
    // This can't be singleton as interceptors added are retained
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

    @Provides
    @Singleton
    @Named(ANONYMOUS)
    fun provideAnonymousOkHttpClient(
        builder: OkHttpClient.Builder
    ): OkHttpClient = builder.build()

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        @Named(ANONYMOUS) okHttpClient: OkHttpClient,
        moshi: Moshi,
        url: HttpUrl
    ): Retrofit.Builder =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxCallAdapterWrapperFactory.createAsync())
            .baseUrl(url)
            .client(okHttpClient)
}
