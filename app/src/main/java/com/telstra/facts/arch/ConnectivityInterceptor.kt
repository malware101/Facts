package com.telstra.facts.arch

import android.content.Context
import com.telstra.facts.arch.util.NetworkUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectivityInterceptor(private val mContext: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var builder = chain.request().newBuilder()
        if (!NetworkUtil.isOnline(mContext)) {
            builder.header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 1)
                .build() // 1 day stale
        } else {
            builder.header("Cache-Control", "public, max-age=30")// read from cache for 30 seconds
        }
        return chain.proceed(builder.build())
    }

}