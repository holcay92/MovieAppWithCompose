package com.example.movieapp.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.databinding.VideoItemBinding
import com.example.movieapp.model.videos.VideoResult
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class VideoAdapter : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private var videoList = ArrayList<VideoResult>()

    inner class VideoViewHolder(itemView: VideoItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        fun bind(video: VideoResult) {
            val bindingItem = VideoItemBinding.bind(itemView)
            bindingItem.apply {
                bindingItem.youtubePlayerView.addYouTubePlayerListener(object :
                    AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        val videoId = video.key
                        youTubePlayer.loadVideo(videoId, 0f)
                    }
                })
            }
            Log.d("TAG_X", "bind video: $video")
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

    fun updateList(list: List<VideoResult?>?) {
        videoList.clear()
        videoList.addAll((list ?: emptyList()) as Collection<VideoResult>)
        Log.d("TAG_X", "updateList video: $videoList")
    }
}
