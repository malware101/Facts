package com.telstra.facts.arch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Base class for result view models which sends [OUT] data as a result.
 */
abstract class BaseResultViewModel<OUT> : ViewModel() {
    private val _result = MutableLiveData<LiveEvent<OUT>>()
    val result: LiveData<LiveEvent<OUT>> = _result

    fun complete(value: OUT) {
        _result.value = LiveEvent(value)
    }
}

/**
 * Result view model which sends just an event without any meaningful event data.
 */
open class VoidResultViewModel : BaseResultViewModel<Unit>() {
    fun complete() = complete(Unit)
}
