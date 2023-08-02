package com.example.movieapp.utils

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class ScalePageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val scaleFactor = 0.85f // Adjust the scale factor as needed

        when {
            position < -1 -> { // Off the left side
                page.alpha = 1f
            }

            position <= 1 -> { // Page is visible
                val scale = if (position < 0) {
                    1 + position * scaleFactor
                } else {
                    1 - position * scaleFactor
                }

                page.scaleX = scale
                page.scaleY = scale

                page.alpha = 1 - Math.abs(position)
            }

            else -> { // Off the right side
                page.alpha = 1f
            }
        }
    }
}
