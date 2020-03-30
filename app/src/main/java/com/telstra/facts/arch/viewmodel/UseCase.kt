package com.telstra.facts.arch.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import com.telstra.facts.arch.util.safeDispose
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.util.UUID

interface UseCaseSchedulers {
    val subscribeScheduler: Scheduler
    val observeScheduler: Scheduler
}

/**
 * Class encapsulating single business use case (call to a backend, local logic, etc.)
 * It is based on Rx observable. It provides information about loading state, any errors occurred
 * or data returned from the call by means of [MutableLiveData] arguments, passed into [invoke]
 * method.
 * Invocation of a use case can be cancelled by [cancel] method.
 *
 * @param IN Type of input (if there are more than one value needed to call a use case,
 *           it is wise to wrap those values into one model class)
 * @param OUT Type of output
 */
abstract class UseCase<IN, OUT>(private val schedulers: UseCaseSchedulers) {
    private var disposables = mutableMapOf<String, Disposable>()

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    abstract fun observable(params: IN): Observable<OUT>

    fun invoke(
        params: IN,
        dataConsumer: Consumer<OUT>,
        errorConsumer: Consumer<Throwable>,
        onStart: () -> Unit
    ): String = if (!allowParallelExecution && disposables.isNotEmpty()) {
        disposables.keys.first()
    } else {
        onStart.invoke()

        val uuid = UUID.randomUUID().toString()
        val disposable = observable(params)
            .subscribeOn(schedulers.subscribeScheduler)
            .observeOn(schedulers.observeScheduler)
            .doOnNext { }
            .doAfterTerminate {
                disposables.remove(uuid)
            }
            .subscribe(dataConsumer, errorConsumer)

        disposables[uuid] = disposable
        uuid
    }

    fun cancel(disposableId: String? = null) {
        if (disposableId != null) {
            val disposable = disposables[disposableId]
            disposable.safeDispose()
            disposables.remove(disposableId)
        } else {
            disposables.forEach {
                it.value.safeDispose()
            }
            disposables.clear()
        }
    }

    /**
     * If [notifyLoadingState] value is false, then loading live data will not be triggered on
     * loading state changes.
     *
     * By default this value is true.
     */
    open var notifyLoadingState = true

    /**
     * If [notifyErrors] value is false, then error live data will not be called if error occurs.
     *
     * By default this value is true.
     */
    open var notifyErrors = true

    /**
     * If [allowParallelExecution] is false, then any subsequent call to this [UseCase] will
     * be discarded if previous call is still running.
     */
    protected open var allowParallelExecution = false
}
