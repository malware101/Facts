package com.telstra.facts.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import com.telstra.facts.arch.viewmodel.BaseResultViewModel
import com.telstra.facts.arch.viewmodel.UseCaseViewModel
import javax.inject.Inject

/**
 * Base fragment class with view model which uses data binding.
 *
 * @param B Type of data binding class used in this fragment
 */
abstract class UseCaseBindingFragment<B : ViewDataBinding, VM : UseCaseViewModel, R : BaseResultViewModel<*>> :
    UseCaseFragment<VM, R>() {

    protected lateinit var binding: B

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        initBinding(requireActivity(), binding)
        binding.lifecycleOwner = this
        return binding.root
    }

    protected abstract fun initBinding(activity: FragmentActivity, binding: B)
}
