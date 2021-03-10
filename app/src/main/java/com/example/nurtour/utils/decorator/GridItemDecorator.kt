package com.example.nurtour.utils.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridItemDecorator : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            when {
                parent.getChildAdapterPosition(view) == 0 -> {
                    top = 36
                    left = 72
                    right = 36
                    bottom = 36
                }
                parent.getChildAdapterPosition(view) == 1 -> {
                    top = 36
                    right = 72
                    left = 16
                    bottom = 36
                }
                parent.getChildAdapterPosition(view) % 2 != 0 -> {
                    right = 72
                    left = 16
                    bottom = 36
                }
                else -> {
                    left = 72
                    right = 36
                    bottom = 36
                }
            }
        }
    }

}