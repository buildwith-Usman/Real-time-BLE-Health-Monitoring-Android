package com.app.dbraze.utils

import android.animation.ObjectAnimator
import android.content.Context
import android.widget.ImageView

class CircularAnimation(private val context: Context, private val imageView: ImageView) {

    private var animator: ObjectAnimator? = null

    init {
        setupAnimator()
    }

    private fun setupAnimator() {
        animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f).apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
        }
    }

    // Start the circular animation
    fun startAnimation() {
        animator?.start()
    }

    // Stop the circular animation
    fun stopAnimation() {
        animator?.cancel() // or animator?.end()
    }
}
