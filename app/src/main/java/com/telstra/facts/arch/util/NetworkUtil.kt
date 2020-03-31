@file:Suppress("DEPRECATION")

package com.telstra.facts.arch.util

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtil {

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}