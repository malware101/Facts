package com.telstra.facts.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
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

class FactsListFragment :
    UseCaseBindingFragment<FactsListFragmentBinding, FactsListViewModel, VoidResultViewModel>() {

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
        viewModel.data.observe(this) { facts ->
            setActionBarTitle(facts.title)
            factsListAdapter.setItems(createFactItems(facts.rows))
        }

        // Set Pull To Refresh View
        swipeToRefresh.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.ghost
            )
        )
        swipeToRefresh.setColorSchemeColors(Color.WHITE)
        swipeToRefresh.setOnRefreshListener {
            factsListAdapter.clearItems()
            viewModel.load(Unit)
            swipeToRefresh.isRefreshing = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.load(Unit)
    }

    private fun setActionBarTitle(toolbarTitle: String?) {
        if (!toolbarTitle.isNullOrBlank()) {
            (activity as FactsActivity?)?.setActionBarTitle(toolbarTitle)
        }
    }

    private fun createFactItems(facts: List<Fact>) = facts.mapNotNull { fact ->
        if (!fact.title.isNullOrBlank() && !fact.description.isNullOrBlank()) {
            FactsItem(fact.title ?: "", fact.description ?: "", fact.imageHref ?: "") {}
        } else null
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveState(outState)
    }
}
