package com.example.movieapp.view.homePage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.* // ktlint-disable no-wildcard-imports
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.* // ktlint-disable no-wildcard-imports
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.fragment.app.Fragment
import com.example.movieapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SplashScreen()
            }
        }
    }
}

@Composable
fun SplashScreen() {
    val fadeInDuration = 3000
    val logoAnimationDuration = 2000

    var logoVisible by remember { mutableStateOf(false) }

    val fadeInAnimation = rememberInfiniteTransition(label = "")
    val alpha by fadeInAnimation.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = fadeInDuration,
                easing = LinearEasing,
            ),
        ),
        label = "",
    )

    val logoAnimation = rememberInfiniteTransition(label = "")
    val logoOffsetX by logoAnimation.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = logoAnimationDuration
                0f at 0
                1f at logoAnimationDuration
            },
        ),
        label = "",
    )
    val logoOffsetY by logoAnimation.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = logoAnimationDuration
                0f at 0
                1f at logoAnimationDuration
            },
        ),
        label = "",
    )
    val logoScale by logoAnimation.animateFloat(
        initialValue = 4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = logoAnimationDuration
                4f at 0
                0f at logoAnimationDuration
            },
        ),
        label = "",
    )

    LaunchedEffect(true) {
        delay(500) // Delay before starting the animations
        logoVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.light_theme)),
    ) {
        val tmdbIcon = painterResource(id = R.drawable.tmdb_icon)
        if (logoVisible) {
            Icon(
                painter = tmdbIcon,
                contentDescription = null,
                modifier = Modifier.background(colorResource(id = R.color.transparent))
                    .offset(
                        x = (logoOffsetX) * 50.dp,
                        y = (logoOffsetY) * 50.dp,
                    )
                    .alpha(alpha)
                    .size((500 * logoScale).dp),
            )
        }
    }
}
