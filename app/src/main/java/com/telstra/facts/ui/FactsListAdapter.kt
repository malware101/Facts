package com.telstra.facts.ui

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.telstra.facts.R
import com.telstra.facts.arch.ui.SectionedMenuAdapter
import com.telstra.facts.arch.ui.SectionedMenuItemHolder
import com.telstra.facts.databinding.FactsItemBinding

class FactsListAdapter(
    private val requestBuilder: RequestBuilder<Drawable>
) : SectionedMenuAdapter<FactsItem>(), ListPreloader.PreloadModelProvider<FactsItem> {

    override fun areItemsInDifferentSection(item: Item, other: Item): Boolean = false

    override fun inflateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SectionedMenuItemHolder<FactsItem> =
        FactsItemViewHolder(FactsItemBinding.inflate(LayoutInflater.from(parent.context)))

    override fun getPreloadItems(position: Int): List<FactsItem> =
        items.subList(position, position + 1)

    override fun getPreloadRequestBuilder(item: FactsItem): RequestBuilder<Drawable> =
        if (item.fullLogoUrl != null) {
            requestBuilder
                .load(item.fullLogoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_logo_placeholder)
                .apply(RequestOptions().fitCenter())
        } else {
            requestBuilder
                .load(R.drawable.ic_logo_placeholder)
                .apply(RequestOptions().fitCenter())
        }

    inner class FactsItemViewHolder(private val binding: FactsItemBinding) :
        SectionedMenuItemHolder<FactsItem>(binding.root) {
        override fun bindItem(item: FactsItem) {
            binding.apply {
                fact = item
                getPreloadRequestBuilder(item).into(logoIv)
                if (item.caption.isNullOrBlank()) {
                    descriptionTv.visibility = View.GONE
                } else {
                    descriptionTv.visibility = View.VISIBLE
                }
            }
        }
    }
}

class FactsItem(
    val title: String?,
    val caption: String?,
    val fullLogoUrl: String?,
    val onClick: () -> Unit
) : SectionedMenuAdapter.Item() {
    override fun isSame(item: SectionedMenuAdapter.Item): Boolean {
        if (item.javaClass != javaClass) return false

        val factsItem = item as FactsItem
        return this.title == factsItem.title &&
                this.caption == factsItem.caption &&
                this.fullLogoUrl == factsItem.fullLogoUrl
    }
}
