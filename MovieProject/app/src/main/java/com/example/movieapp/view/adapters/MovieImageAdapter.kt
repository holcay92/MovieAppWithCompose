package com.example.movieapp.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.ItemImageBinding
import com.example.movieapp.model.movieImages.Poster

class MovieImageAdapter : RecyclerView.Adapter<MovieImageAdapter.MovieImageViewHolder>() {

    private var movieImageList = ArrayList<Poster>()

    class MovieImageViewHolder(itemImage: ItemImageBinding) :
        RecyclerView.ViewHolder(itemImage.root) {

        fun bind(image: Poster) {
            val itemImage = ItemImageBinding.bind(itemView)
            itemImage.apply {
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500${image.file_path}").fitCenter()
                    .into(imageView!!)
               // Log.d("TAG_X", "bind in the adapter image.file_path : ${image.file_path}")
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MovieImageViewHolder {
        val itemView =
            ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieImageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieImageViewHolder, position: Int) {
        holder.bind(movieImageList[position])
    }

    override fun getItemCount() = movieImageList.size

    fun updateList(list: List<Poster>?) {
        movieImageList.clear()
        movieImageList.addAll(list ?: emptyList())
       // Log.d("TAG_X", "Adapter Movie updateList in the adapter list: $movieImageList")
        notifyDataSetChanged()
    }
}
