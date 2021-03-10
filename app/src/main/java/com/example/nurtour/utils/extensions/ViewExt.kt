package com.example.nurtour.utils.extensions

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

/**
 *  view экшны для эффекта нажатия на элемента
 */
@SuppressLint("ClickableViewAccessibility")
fun View.generateActions(
    actionUp: (View, MotionEvent) -> Unit,
    actionDown: (View, MotionEvent) -> Unit,
    actionCancel: (View) -> Unit
) {
    this.setOnTouchListener { v, event ->
        v.requestFocus()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                actionDown(v, event)
            }
            MotionEvent.ACTION_UP -> {
                actionUp(v, event)
            }
            MotionEvent.ACTION_CANCEL -> {
                actionCancel(v)
            }
        }
        true
    }
}

