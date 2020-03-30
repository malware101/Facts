package com.telstra.facts.arch.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.telstra.facts.arch.util.distinct
import io.reactivex.functions.Consumer

/**
 * Base view model class which has:
 * - [LiveData] for storing information about current loading state
 * - [LiveData] for storing errors which happened while performing business logic
 *
 * Any logic performed by view model relies on separate [UseCase] instances, each of which performs
 * one particular task.
 */
abstract class UseCaseViewModel(vararg useCases: UseCase<*, *>) : ViewModel() {
    protected val _isLoading = LoadingLiveData()
    val isLoading: LiveData<Boolean> = _isLoading.boolValue

    protected val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    private val useCaseMap = mutableMapOf<String, UseCase<*, *>>()

    init {
        useCases.forEach { useCase -> useCaseMap[useCase.javaClass.toString()] = useCase }
        _isLoading.reset()
    }

    @Suppress("UNCHECKED_CAST")
    protected fun <IN, OUT, UC : UseCase<IN, OUT>> submit(
        params: IN,
        clazz: Class<UC>,
        data: MutableLiveData<OUT>,
        isLoading: LoadingLiveData = _isLoading,
        error: MutableLiveData<Throwable> = _error
    ): String {
        val name = clazz.toString()
        val useCase = useCaseMap[name] as? UseCase<IN, OUT>
        return if (useCase == null) {
            val msg = "Couldn't find use case [$name] for param [$params]"
            _error.value = Throwable(msg)
            ""
        } else {
            submit(params, useCase, data, isLoading, error)
        }
    }

    protected fun <IN, OUT> submit(
        params: IN,
        useCase: UseCase<IN, OUT>,
        data: MutableLiveData<OUT>,
        isLoading: LoadingLiveData = _isLoading,
        error: MutableLiveData<Throwable> = _error
    ): String {
        return useCase.invoke(params,
            Consumer { value -> processData(useCase, isLoading, data, value) },
            Consumer { throwable -> processUseCaseError(useCase, isLoading, error, throwable) }) {
            // Start loading only if use case is actually starting its work.
            // It may not do so in case if it is called again while previous call is still running
            // and parallel execution is not enabled.
            processUseCaseLoading(useCase, isLoading, true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        useCaseMap.values.forEach { it.cancel() }
    }

    /**
     * Defines logic for processing use case data changes. Can be overridden in subclasses to modify
     * behaviour.
     *
     * By default, set new value to [data] and calls [processUseCaseLoading] with *false* value.
     *
     * Subclasses can use this method to chain several calls instead of relying on live data
     * observers to make the next call.
     */
    protected open fun <OUT> processData(
        useCase: UseCase<*, OUT>,
        isLoading: LoadingLiveData,
        data: MutableLiveData<OUT>,
        value: OUT
    ) {
        data.value = value
        processUseCaseLoading(useCase, isLoading, false)
    }

    /**
     * Defines logic for processing use case exception. Can be overridden in subclasses to modify
     * behaviour.
     *
     * By default, set new value to [error] if [UseCase.notifyErrors] is *true* and calls
     * [processUseCaseLoading] with *false* value.
     */
    protected open fun processUseCaseError(
        useCase: UseCase<*, *>,
        isLoading: LoadingLiveData,
        error: MutableLiveData<Throwable>,
        throwable: Throwable
    ) {
        if (useCase.notifyErrors) error.value = throwable
        processUseCaseLoading(useCase, isLoading, false)
    }

    /**
     * Defines logic for processing loading state changes. Can be overridden in subclasses to modify
     * behaviour.
     *
     * By default, calls [processNonUseCaseLoading] with the *key* equal to class name of
     * the [useCase].
     */
    protected open fun processUseCaseLoading(
        useCase: UseCase<*, *>,
        isLoading: LoadingLiveData,
        value: Boolean
    ) {
        if (useCase.notifyLoadingState) {
            processNonUseCaseLoading(useCase.javaClass.simpleName, isLoading, value)
        }
    }

    /**
     * Defines logic for processing loading state changes.
     *
     * Call [LoadingLiveData.startLoading] or [LoadingLiveData.stopLoading] with provided [key]
     * value based on [value].
     */
    protected fun processNonUseCaseLoading(
        key: String,
        isLoading: LoadingLiveData,
        value: Boolean
    ) {
        if (value) {
            isLoading.startLoading(key)
        } else {
            isLoading.stopLoading(key)
        }
    }

    /**
     * Defines logic for processing non use case exception.
     *
     * Sets new value to [error] and calls [processNonUseCaseLoading] with *false* value.
     */
    protected fun processNonUseCaseError(
        key: String,
        isLoading: LoadingLiveData,
        error: MutableLiveData<Throwable>,
        throwable: Throwable
    ) {
        error.value = throwable
        processNonUseCaseLoading(key, isLoading, false)
    }

    open fun saveState(bundle: Bundle) {}

    open fun restoreState(bundle: Bundle) {}
}

/**
 * Simple view model class has only one use case assigned to it.
 *
 * @param IN  Input type to be used for single use case of this view model
 * @param OUT Result type of data stored by this view model. Results are propagated back
 *            to view by [data] [LiveData] property.
 */
abstract class SingleUseCaseViewModel<IN, OUT>(private val useCase: UseCase<IN, OUT>) :
    UseCaseViewModel(useCase) {
    protected val _data = MutableLiveData<OUT>()
    val data: LiveData<OUT> = _data

    protected fun submit(params: IN) = submit(params, useCase, _data)
}

/**
 * Convenient wrapper around [LiveData] to track loading state. Properly tracks loading state
 * in case when one use case started loading, then another one started loading and the first one
 * stopped loading.
 */
class LoadingLiveData : MutableLiveData<Set<String>>() {
    val boolValue: LiveData<Boolean> = Transformations.map(this) { it.isNotEmpty() }.distinct()

    init {
        reset()
    }

    fun reset() {
        value = setOf()
    }

    fun startLoading(key: String) {
        val currentValue = value ?: throw IllegalArgumentException("value cannot be null")
        value = currentValue.plus(key)
    }

    fun stopLoading(key: String) {
        val currentValue = value ?: throw IllegalArgumentException("value cannot be null")
        value = currentValue.minus(key)
    }
}
