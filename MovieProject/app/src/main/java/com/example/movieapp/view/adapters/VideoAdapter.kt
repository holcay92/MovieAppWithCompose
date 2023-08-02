package com.example.movieapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.databinding.VideoItemBinding
import com.example.movieapp.model.videos.VideoResult

class VideoAdapter : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private var videoList = ArrayList<VideoResult>()

    inner class VideoViewHolder(itemView: VideoItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        fun bind(video: VideoResult) {
            val bindingItem = VideoItemBinding.bind(itemView)
            bindingItem.apply {
                // youtubePlayerView
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
}
