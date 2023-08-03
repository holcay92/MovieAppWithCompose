package com.example.movieapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.databinding.VideoItemBinding
import com.example.movieapp.model.videos.VideoResult
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class VideoAdapter(
    private val lifecycle: Lifecycle,
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private var videoList = ArrayList<VideoResult>()

    inner class VideoViewHolder(itemView: VideoItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val youTubePlayerView: YouTubePlayerView = itemView.youtubePlayerView

        fun bind(video: VideoResult) {
            val bindingItem = VideoItemBinding.bind(itemView)
            lifecycle.addObserver(youTubePlayerView)

            youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadOrCueVideo(lifecycle, video.key, 0F)
                }
            })

            bindingItem.apply {
                bindingItem.youtubePlayerView.addYouTubePlayerListener(object :
                    AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        val videoId = video.key
                        youTubePlayer.loadVideo(videoId, 0f)
                    }
                })
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): VideoAdapter.VideoViewHolder {
        val itemView =
            VideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VideoAdapter.VideoViewHolder, position: Int) {
        holder.bind(videoList[position])
    }

    override fun getItemCount() = videoList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<VideoResult>?) {
        videoList.clear()
        videoList.addAll(list ?: emptyList())
        notifyDataSetChanged()
    }
}
