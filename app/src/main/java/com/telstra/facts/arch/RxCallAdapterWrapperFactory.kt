package com.telstra.facts.arch

import io.reactivex.*
import io.reactivex.functions.Function
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type
import kotlin.reflect.KClass

const val STATUS_CODE_UNPROCESSABLE_ENTITY = 422

@Retention(AnnotationRetention.RUNTIME)
annotation class RxErrorType(val type: KClass<*>)

class RxNetworkException(
    val error: Any?,
    val statusCode: Int,
    throwable: Throwable
) : RuntimeException(throwable)


internal class RxCallAdapterWrapperFactory constructor(
    private val rxJava2CallAdapterFactory: RxJava2CallAdapterFactory
) : CallAdapter.Factory() {

    companion object {
        fun createAsync(): RxCallAdapterWrapperFactory {
            return RxCallAdapterWrapperFactory(RxJava2CallAdapterFactory.createAsync())
        }
    }

    private fun handleError(annotations: Array<Annotation>, retrofit: Retrofit, throwable: Throwable): Throwable {
        val errorResponse: RxErrorType? = annotations.find { it is RxErrorType } as? RxErrorType

        return if (errorResponse != null && throwable is HttpException) {
            val error = parseError(retrofit, throwable, errorResponse.type)
            RxNetworkException(error, throwable.code(), throwable)
        } else {
            throwable
        }
    }

    private fun parseError(retrofit: Retrofit, httpException: HttpException, kClass: KClass<*>): Any? {
        if (httpException.response()?.isSuccessful == true) {
            return null
        }
        val errorBody = httpException.response()?.errorBody() ?: return null
        val converter: Converter<ResponseBody, Any> = retrofit.responseBodyConverter(kClass.java, arrayOf())
        return converter.convert(errorBody)
    }

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        @Suppress("UNCHECKED_CAST")
        val rxJava2CallAdapter: CallAdapter<Any, Any> = rxJava2CallAdapterFactory.get(returnType, annotations, retrofit) as CallAdapter<Any, Any>
        return RxCallAdapterWrapper(annotations, retrofit, rxJava2CallAdapter)
    }

    private inner class RxCallAdapterWrapper constructor(
        private val annotations: Array<Annotation>,
        private val retrofit: Retrofit,
        private val callAdapter: CallAdapter<Any, Any>
    ) : CallAdapter<Any, Any> {

        override fun adapt(call: Call<Any>): Any {
            val rxCall = callAdapter.adapt(call)

            return when (rxCall) {
                is Observable<*> -> rxCall.onErrorResumeNext(Function { Observable.error(handleError(annotations, retrofit, it)) })
                is Maybe<*> -> rxCall.onErrorResumeNext(Function { Maybe.error(handleError(annotations, retrofit, it)) })
                is Single<*> -> rxCall.onErrorResumeNext(Function { Single.error(handleError(annotations, retrofit, it)) })
                is Flowable<*> -> rxCall.onErrorResumeNext(Function { Flowable.error(handleError(annotations, retrofit, it)) })
                is Completable -> rxCall.onErrorResumeNext(Function { Completable.error(handleError(annotations, retrofit, it)) })
                else -> rxCall
            }
        }

        override fun responseType(): Type {
            return callAdapter.responseType()
        }
    }
}