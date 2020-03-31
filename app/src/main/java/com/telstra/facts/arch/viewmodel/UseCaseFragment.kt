package com.telstra.facts.arch.viewmodel

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import com.telstra.facts.arch.ui.BaseFragment
import com.telstra.facts.arch.util.showException
import javax.inject.Inject

/**
 * Base fragment class with [UseCaseViewModel] attached to it.
 *
 * Creates one-to-one connection between fragment and view model.
 *
 * Sub-classes will in most cases override [viewModel] property by injecting required view model, e.g.
 *
 *      @Inject override lateinit var viewModel: MyViewModel
 *
 * @param VM Sub-class of [UseCaseViewModel] to be used by this fragment
 */
abstract class UseCaseFragment<VM : UseCaseViewModel, RVM : BaseResultViewModel<*>> :
    BaseFragment<RVM>() {
    protected abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restoreViewModelState(savedInstanceState)
        initUseCaseViewModel(view, viewModel)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        restoreViewModelState(savedInstanceState)
    }

    private fun restoreViewModelState(savedInstanceState: Bundle?) {
        savedInstanceState?.run {
            viewModel.restoreState(this)
        }
    }

    protected abstract fun initUseCaseViewModel(view: View, viewModel: VM)

    // This is a workaround the issue with multiple same classes being generated if there is a middle class
    // in Dagger hierarchy without @Inject fields.
    // https://github.com/google/dagger/issues/814
    @Inject
    override lateinit var dummy: DaggerInjectionWorkaroundField
}

