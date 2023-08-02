package com.example.movieapp.utils

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.movieapp.R
import kotlin.math.abs

class DepthPageTransformer : ViewPager2.PageTransformer {
    private val MIN_SCALE = 0.75f
    private val MIN_ALPHA = 0.5f

    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val pageHeight = page.height

        when {
            position < -1 -> { // Off the left side
                page.alpha = 0f
            }

            position <= 1 -> { // Page is visible
                page.alpha = 1f
                page.pivotY = pageHeight / 2f
                page.pivotX = pageWidth / 2f
                page.rotationY = 45 * position // Adjust the rotation factor as needed
                page.translationX = -pageWidth * position

                // Scale the page down (between MIN_SCALE and 1)
                val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                page.scaleX = scaleFactor
                page.scaleY = scaleFactor

                // Adjust the alpha based on position
                val alphaFactor = (
                    MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)
                    )
                page.alpha = alphaFactor

                // Set the background to opaque for the current page
                if (position == 0f) {
                    page.setBackgroundColor(
                        ContextCompat.getColor(
                            page.context,
                            R.color.light_theme,
                        ),
                    )
                } else {
                    page.setBackgroundColor(Color.TRANSPARENT)
                }
            }

            else -> { // Off the right side
                page.alpha = 0f
                page.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }
}
