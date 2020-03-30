package com.telstra.facts.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.telstra.facts.R
import com.telstra.facts.arch.ui.SectionedMenuItemDivider
import com.telstra.facts.arch.util.observe
import com.telstra.facts.arch.viewmodel.UseCaseBindingFragment
import com.telstra.facts.arch.viewmodel.VoidResultViewModel
import com.telstra.facts.databinding.FactsListFragmentBinding
import com.telstra.facts.model.Fact
import com.telstra.facts.viewmodel.FactsListViewModel
import kotlinx.android.synthetic.main.facts_list_fragment.*
import javax.inject.Inject

class FactsListFragment
//    : BaseFragment<VoidResultViewModel> (){
    : UseCaseBindingFragment<FactsListFragmentBinding, FactsListViewModel, VoidResultViewModel>() {

    companion object {
        private const val PRELOAD_SIZE = 50
    }

    private lateinit var factsListAdapter: FactsListAdapter

    override val layoutResId = R.layout.facts_list_fragment

    @Inject
    override lateinit var viewModel: FactsListViewModel

    override fun initBinding(activity: FragmentActivity, binding: FactsListFragmentBinding) {
        binding.viewModel = viewModel
    }

    override fun initUseCaseViewModel(view: View, viewModel: FactsListViewModel) {
        setListAdapter()
        viewModel.data.observe(this) {
            factsListAdapter.setItems(createFactItems(it))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.load(Unit)
    }

    private fun createFactItems(facts: List<Fact>) = facts.map { fact ->
        FactsItem(fact.title, fact.description, fact.imageHref) {}
    }

    private fun setListAdapter() {
        val glide = Glide.with(this)
        factsListAdapter = FactsListAdapter(glide.asDrawable())
        val sizeProvider = ViewPreloadSizeProvider<FactsItem>()
        val preloader = RecyclerViewPreloader(glide, factsListAdapter, sizeProvider, PRELOAD_SIZE)

        factsRecyclerView.apply {
            addOnScrollListener(preloader)

            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SectionedMenuItemDivider(context))
            adapter = factsListAdapter
        }
    }

}
