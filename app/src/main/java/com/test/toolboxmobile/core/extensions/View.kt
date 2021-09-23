package com.test.toolboxmobile.core.extensions

import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewPropertyAnimator

const val DEFAULT_ANIMATION_DURATION_TIME = 250L
//show
fun View.show() {
    visibility = View.VISIBLE
}

//hide
fun View.hide() {
    visibility = View.GONE
}

//fadeOut
fun View.fadeOut(
    duration: Long = DEFAULT_ANIMATION_DURATION_TIME
) = fadeOut(this, duration)
    .apply { setListener(object : AnimatorListenerAdapter() {}) }
    .start()

fun View.fadeOut(
    duration: Long = DEFAULT_ANIMATION_DURATION_TIME,
    animListener: AnimatorListenerAdapter
) = fadeOut(this, duration)
    .apply { setListener(animListener) }
    .start()

private fun fadeOut(
    view: View, duration: Long
): ViewPropertyAnimator = view.apply {
    alpha = 1.0f
}
    .animate()
    .setDuration(duration)
    .alpha(0.0f)