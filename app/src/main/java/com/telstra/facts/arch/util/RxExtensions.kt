package com.telstra.facts.arch.util

import android.util.Log
import io.reactivex.SingleEmitter
import io.reactivex.disposables.Disposable

fun Disposable?.safeDispose() = if (this != null && !isDisposed) {
    dispose()
    true
} else false

fun <T> SingleEmitter<T>.safeOnSuccess(value: T) =
    if (!isDisposed) {
        onSuccess(value)
    } else {
        Log.d("Single.onSuccess()", "already disposed")
    }

fun <T> SingleEmitter<T>.safeOnError(value: Throwable) =
    if (!isDisposed) {
        onError(value)
    } else {
        Log.d("Single.onError()", "already disposed")
    }
