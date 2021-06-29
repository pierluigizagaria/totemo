package com.pierluigizagaria.totemo.utils

import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class GridSpacingItemDecoration(
    context: Context,
    private val spanCount: Int,
    dp: Int
) : ItemDecoration() {

    private val spacing: Int

    init {
        spacing = convertDpToPixel(dp, context)
    }

    fun convertDpToPixel(dp: Int, context: Context): Int {
        return dp * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount // item column
        outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
        outRect.right =
            spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
        if (position >= spanCount) {
            outRect.top = spacing // item top
        }
    }
}