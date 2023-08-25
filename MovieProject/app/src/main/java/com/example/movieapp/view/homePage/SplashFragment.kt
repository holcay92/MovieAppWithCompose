package com.example.movieapp.view.homePage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.* // ktlint-disable no-wildcard-imports
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.* // ktlint-disable no-wildcard-imports
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.times
import androidx.fragment.app.Fragment
import com.example.movieapp.R
import dagger.hilt.android.AndroidEntryPoint

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
    val rotationState = rememberInfiniteTransition(label = "")
    val rotation by rotationState.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(2000)),
        label = "",
    )

    val tmdbIcon = painterResource(id = R.drawable.tmdb_icon)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.light_theme))
            .graphicsLayer(rotationZ = rotation),
    ) {
        Icon(
            painter = tmdbIcon,
            contentDescription = null,
            modifier = Modifier
                .background(colorResource(id = R.color.transparent))
                .fillMaxSize(),
        )
    }
}
