package com.telstra.facts.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.telstra.facts.R
import com.telstra.facts.arch.BaseFragment
import com.telstra.facts.arch.UseCaseBindingFragment
import com.telstra.facts.arch.util.observe
import com.telstra.facts.arch.viewmodel.VoidResultViewModel
import com.telstra.facts.databinding.FactsListFragmentBinding
import com.telstra.facts.viewmodel.FactsListViewModel
import javax.inject.Inject

class FactsListFragment
//    : BaseFragment<VoidResultViewModel> (){
    : UseCaseBindingFragment<FactsListFragmentBinding, FactsListViewModel, VoidResultViewModel>() {

    override val layoutResId = R.layout.facts_list_fragment

    @Inject
    override lateinit var viewModel: FactsListViewModel

    override fun initBinding(activity: FragmentActivity, binding: FactsListFragmentBinding) {
        binding.viewModel = viewModel
    }

    override fun initUseCaseViewModel(view: View, viewModel: FactsListViewModel) {
        viewModel.data.observe(this) {
            Log.d("test", it.size.toString())
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.load(Unit)
    }
}
