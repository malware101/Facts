package com.telstra.facts.arch.viewmodel

import android.os.Bundle
import javax.inject.Inject

class MainViewModel
@Inject constructor() : UseCaseViewModel() {


    override fun saveState(bundle: Bundle) {
        // Add additional states
    }

    override fun restoreState(bundle: Bundle) {
        // Add additional states
    }

    override fun onCleared() {
        super.onCleared()
    }
}
