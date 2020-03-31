package com.telstra.facts.arch

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule(private val context: Context) {

    companion object {
        const val ANONYMOUS = "ANONYMOUS"
        const val LOGGER_FULL = "LOGGER_FULL"

        private const val CONNECT_TIMEOUT = 30L
        private const val READ_TIMEOUT = 30L
        private const val WRITE_TIMEOUT = 30L

        private const val CACHE_SIZE = (5 * 1024 * 1024).toLong()
        private const val BASE_URL = "BASE_URL"
    }

    private val cache = Cache(context.cacheDir, CACHE_SIZE)

    @Provides
    @Singleton
    @Named(LOGGER_FULL)
    fun provideLogger(): Interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    // This can't be singleton as interceptors added are retained
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(ConnectivityInterceptor(context))
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

    @Provides
    @Singleton
    @Named(ANONYMOUS)
    fun provideAnonymousOkHttpClient(
        builder: OkHttpClient.Builder,
        @Named(LOGGER_FULL) logger: Interceptor
    ): OkHttpClient = builder.addInterceptor(logger).build()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().apply {
        add(KotlinJsonAdapterFactory())
    }.build()

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        @Named(ANONYMOUS) okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit.Builder =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxCallAdapterWrapperFactory.createAsync())
            .client(okHttpClient)

    @Provides
    @Named(BASE_URL)
    fun provideBaseUrl(): String = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/"

    @Provides
    @Singleton
    fun provideTelstraApiRetrofit(
        builder: Retrofit.Builder,
        @Named(BASE_URL) url: String
    ): Retrofit =
        builder.baseUrl(url).build()

}
