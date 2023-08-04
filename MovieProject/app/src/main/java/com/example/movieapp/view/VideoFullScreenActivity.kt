package com.example.movieapp.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.movieapp.R
import com.example.movieapp.databinding.ActivityFullScreenBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class VideoFullScreenActivity : AppCompatActivity() {

    private lateinit var youTubePlayer: YouTubePlayer
    private lateinit var binding: ActivityFullScreenBinding
    private var videoId: String = ""

    private var isFullscreen = false
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isFullscreen) {
                // if the player is in fullscreen, exit fullscreen
                youTubePlayer.toggleFullscreen()
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videoId = intent.getStringExtra("video_id").toString()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        val youTubePlayerView = findViewById<YouTubePlayerView>(R.id.youtube_player_view)
        val fullscreenViewContainer = findViewById<FrameLayout>(R.id.full_screen_view_container)

        val iFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(1)
            .fullscreen(1) // enable full screen button
            .build()
        youTubePlayerView.enableAutomaticInitialization = false

        youTubePlayerView.addFullscreenListener(object : FullscreenListener {
            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                isFullscreen = true

                youTubePlayerView.visibility = View.GONE
                fullscreenViewContainer.visibility = View.VISIBLE
                fullscreenViewContainer.addView(fullscreenView)
            }

            override fun onExitFullscreen() {
                isFullscreen = false
                youTubePlayerView.visibility = View.VISIBLE
                fullscreenViewContainer.visibility = View.GONE
                fullscreenViewContainer.removeAllViews()
            }
        })

        youTubePlayerView.initialize(
            object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    this@VideoFullScreenActivity.youTubePlayer = youTubePlayer
                    youTubePlayer.cueVideo(videoId, 0f)
                    val enterFullscreenButton = findViewById<Button>(R.id.enter_fullscreen_button)
                    enterFullscreenButton.setOnClickListener {
                        youTubePlayer.toggleFullscreen()
                    }
                }
            },
            iFramePlayerOptions,
        )

        lifecycle.addObserver(youTubePlayerView)
    }
}
