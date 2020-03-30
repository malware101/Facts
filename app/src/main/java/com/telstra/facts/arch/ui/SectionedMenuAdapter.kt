package com.telstra.facts.arch.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.telstra.facts.R

enum class SectionedMenuItemPosition {
    SINGLE,
    START,
    MIDDLE,
    END
}

class SectionedMenuItemDivider(
    private val context: Context,
    private val padding: Float = context.resources.getDimension(R.dimen.space_unit_3)
) : RecyclerViewDividerItemDecoration(context, R.color.white, R.color.divider) {

    override fun getLeftInset(view: View, parent: RecyclerView, state: RecyclerView.State) = padding

    override fun getPadding(view: View, parent: RecyclerView, state: RecyclerView.State): Int {
        if (isSectionBreak(view, parent)) {
            return context.resources.getDimensionPixelSize(R.dimen.space_unit_1)
        }
        return super.getPadding(view, parent, state)
    }

    override fun getBackgroundPaint(view: View, parent: RecyclerView, state: RecyclerView.State): Paint {
        if (isSectionBreak(view, parent)) {
            return Paint().apply {
                color = ContextCompat.getColor(context, android.R.color.transparent)
            }
        }
        return super.getBackgroundPaint(view, parent, state)
    }

    override fun getDividerPaint(view: View, parent: RecyclerView, state: RecyclerView.State): Paint {
        if (isSectionBreak(view, parent)) {
            return Paint().apply {
                color = ContextCompat.getColor(context, android.R.color.transparent)
            }
        }
        return super.getDividerPaint(view, parent, state)
    }

    private fun isSectionBreak(view: View, parent: RecyclerView): Boolean {
        (parent.adapter as? SectionedMenuAdapter<*>)?.let { adapter ->
            val position = parent.getChildAdapterPosition(view)
            return adapter.isSectionBreak(position)
        }
        return false
    }
}

abstract class SectionedMenuAdapter<IT : SectionedMenuAdapter.Item>(private val animate: Boolean = true) :
    RecyclerView.Adapter<SectionedMenuItemHolder<IT>>() {

    abstract class Item {
        var position = SectionedMenuItemPosition.MIDDLE
        var sectionGroupId: Int = 0

        abstract fun isSame(item: Item): Boolean
        open fun areContentsSame(item: Item) = false
    }

    protected abstract fun areItemsInDifferentSection(item: Item, other: Item): Boolean

    protected abstract fun inflateViewHolder(parent: ViewGroup, viewType: Int): SectionedMenuItemHolder<IT>

    protected var items = listOf<IT>()
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionedMenuItemHolder<IT> {
        return inflateViewHolder(parent, viewType).apply {
            itemView.layoutParams =
                RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SectionedMenuItemHolder<IT>, position: Int) = holder.bind(items[position])

    private fun isSectionBreak(items: List<Item>, position: Int): Boolean {
        fun List<Item>.getItem(position: Int) = if (position < 0 || position > size - 1) null else items[position]

        items.getItem(position)?.let { item ->
            items.getItem(position + 1)?.let { nextItem ->
                if (areItemsInDifferentSection(item, nextItem)) {
                    return true
                }
            }
        }
        return false
    }

    fun isSectionBreak(position: Int): Boolean = isSectionBreak(items, position)

    fun setItems(items: List<IT>) {
        for (i in items.indices) {
            val item = items[i]
            val prevItem = if (i > 0) items[i - 1] else null
            val nextItem = if (i < items.size - 1) items[i + 1] else null
            if (prevItem == null || areItemsInDifferentSection(prevItem, item)) {
                // Start of a new section
                item.position = SectionedMenuItemPosition.START
            }
            if (nextItem == null || areItemsInDifferentSection(nextItem, item)) {
                // End of a current section
                if (item.position == SectionedMenuItemPosition.START) {
                    item.position = SectionedMenuItemPosition.SINGLE
                } else {
                    item.position = SectionedMenuItemPosition.END
                }
            }
        }

        // Animate changes
        if (animate) {
            val oldItems = this.items
            this.items = items
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val newItem = items[newItemPosition]
                    val oldItem = oldItems[oldItemPosition]
                    return newItem.isSame(oldItem)
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val newItem = items[newItemPosition]
                    val oldItem = oldItems[oldItemPosition]
                    return newItem.areContentsSame(oldItem)
                }

                override fun getOldListSize(): Int = oldItems.size

                override fun getNewListSize(): Int = items.size
            })
            result.dispatchUpdatesTo(this)
        } else {
            this.items = items
            notifyDataSetChanged()
        }
    }
}

abstract class SectionedMenuItemHolder<ITEM : SectionedMenuAdapter.Item>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bindItem(item: ITEM)

    fun bind(item: ITEM) {
        bindItem(item)
    }
}
