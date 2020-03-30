package com.telstra.facts.ui
//
//import android.graphics.drawable.Drawable
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.bumptech.glide.ListPreloader
//import com.bumptech.glide.RequestBuilder
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.request.RequestOptions
//
//class StoresListAdapter(
//    private val requestBuilder: RequestBuilder<Drawable>
//) : SectionedMenuAdapter<StoreItem>(), ListPreloader.PreloadModelProvider<StoreItem> {
//
//    override fun areItemsInDifferentSection(item: Item, other: Item): Boolean = false
//
//    override fun inflateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): SectionedMenuItemHolder<StoreItem> =
//        StoreItemViewHolder(StoreItemBinding.inflate(LayoutInflater.from(parent.context)))
//
//    override fun getPreloadItems(position: Int): List<StoreItem> =
//        items.subList(position, position + 1)
//
//    override fun getPreloadRequestBuilder(item: StoreItem): RequestBuilder<Drawable> =
//        if (item.fullLogoUrl != null) {
//            requestBuilder
//                .load(item.fullLogoUrl)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.drawable.ic_store_logo_placeholder)
//                .apply(RequestOptions().fitCenter())
//        } else {
//            requestBuilder
//                .load(R.drawable.ic_store_logo_placeholder)
//                .apply(RequestOptions().fitCenter())
//        }
//
//    inner class StoreItemViewHolder(private val binding: StoreItemBinding) :
//        SectionedMenuItemHolder<StoreItem>(binding.root) {
//        override fun bindItem(item: StoreItem) {
//            binding.apply {
//                store = item
//                getPreloadRequestBuilder(item).into(storeLogoIv)
//                root.setSingleClickListener { item.onClick() }
//                if (item.caption.isNullOrBlank()) {
//                    storeDescriptionTv.visibility = View.GONE
//                } else {
//                    storeDescriptionTv.visibility = View.VISIBLE
//                }
//            }
//        }
//    }
//}
//
//class StoreItem(
//    val title: String?,
//    val caption: String?,
//    val fullLogoUrl: String?,
//    val onClick: () -> Unit
//) : SectionedMenuAdapter.Item() {
//    override fun isSame(item: SectionedMenuAdapter.Item): Boolean {
//        if (item.javaClass != javaClass) return false
//
//        val storeItem = item as StoreItem
//        return this.title == storeItem.title &&
//                this.caption == storeItem.caption &&
//                this.fullLogoUrl == storeItem.fullLogoUrl
//    }
//}
