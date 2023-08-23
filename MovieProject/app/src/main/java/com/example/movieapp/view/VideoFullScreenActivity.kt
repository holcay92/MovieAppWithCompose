package com.example.movieapp.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions.Builder
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class VideoFullScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide the status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        val videoKey = VideoFullScreenActivityArgs.fromBundle(intent.extras!!).videoId
        val composeView = ComposeView(this)
        setContentView(composeView)
        composeView.setContent {
            FullScreenYoutubePlayer(videoKey)
        }
    }
}

@Composable
fun FullScreenYoutubePlayer(
    videoId: String,
) {
    AndroidView(factory = {
        val view = YouTubePlayerView(it)
        val iFramePlayerOptions: IFramePlayerOptions = Builder() // todo
            .controls(1) // enable full screen button
            .fullscreen(1)
            .build()
        view.addYouTubePlayerListener(
            object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            },
        )
        view
    })
}
