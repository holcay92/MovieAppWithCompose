package com.example.movieapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.movieapp.databinding.MovieImageItemBinding
import com.example.movieapp.model.movieImages.Poster

class MovieImageAdapter : RecyclerView.Adapter<MovieImageAdapter.MovieImageViewHolder>() {

    private var movieImageList = ArrayList<Poster>()

    class MovieImageViewHolder(itemImage: MovieImageItemBinding) :
        RecyclerView.ViewHolder(itemImage.root) {

        fun bind(image: Poster) {
            val itemImage = MovieImageItemBinding.bind(itemView)
            itemImage.apply {
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500${image.file_path}").fitCenter()
                    .transform(
                        RoundedCorners(50),
                    )
                    .into(imageView)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MovieImageViewHolder {
        val itemView =
            MovieImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieImageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieImageViewHolder, position: Int) {
        holder.bind(movieImageList[position])
    }

    override fun getItemCount() = movieImageList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<Poster>?) {
        movieImageList.clear()
        movieImageList.addAll(list ?: emptyList())
        notifyDataSetChanged()
    }
}
