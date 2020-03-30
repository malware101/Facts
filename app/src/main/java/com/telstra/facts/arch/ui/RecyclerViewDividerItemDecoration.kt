package com.telstra.facts.arch.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.telstra.facts.R

open class RecyclerViewDividerItemDecoration constructor(
    context: Context,
    @ColorRes backgroundColorResId: Int,
    @ColorRes dividerColorResId: Int = R.color.white,
    @DimenRes paddingResId: Int = R.dimen.divider_height
) : RecyclerView.ItemDecoration() {

    private val background = Paint().apply {
        color = ContextCompat.getColor(context, backgroundColorResId)
    }
    private val divider = Paint().apply {
        color = ContextCompat.getColor(context, dividerColorResId)
    }
    private val padding: Int = context.resources.getDimensionPixelSize(paddingResId)

    open fun hasDivider(view: View, parent: RecyclerView, state: RecyclerView.State): Boolean {
        val position = parent.getChildAdapterPosition(view)
        parent.adapter?.let {
            return position < it.itemCount - 1
        }
        return false
    }

    open fun getLeftInset(view: View, parent: RecyclerView, state: RecyclerView.State) = 0f

    open fun getRightInset(view: View, parent: RecyclerView, state: RecyclerView.State) = 0f

    open fun getBackgroundPaint(view: View, parent: RecyclerView, state: RecyclerView.State) =
        background

    open fun getDividerPaint(view: View, parent: RecyclerView, state: RecyclerView.State) = divider

    open fun getPadding(view: View, parent: RecyclerView, state: RecyclerView.State) = padding

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val padding = getPadding(view, parent, state)

        if (hasDivider(view, parent, state)) {
            outRect.bottom = padding
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val lm = parent.layoutManager as LinearLayoutManager

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            if (hasDivider(child, parent, state)) {
                drawSingle(c, lm, child, parent, state)
            }
        }
    }

    private fun drawSingle(
        c: Canvas,
        lm: RecyclerView.LayoutManager,
        child: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val padding = getPadding(child, parent, state)
        val divider = getDividerPaint(child, parent, state)

        val bottom = lm.getDecoratedBottom(child) + child.translationY
        val top = bottom - 2 * padding
        val left = lm.getDecoratedLeft(child) + child.translationX
        val right = lm.getDecoratedRight(child) + child.translationX

        val leftInset = getLeftInset(child, parent, state)
        val rightInset = getRightInset(child, parent, state)

        c.drawRect(left, top, right, bottom, getBackgroundPaint(child, parent, state))
        c.drawRect(left + leftInset, top, right - rightInset, bottom, divider)
    }
}
