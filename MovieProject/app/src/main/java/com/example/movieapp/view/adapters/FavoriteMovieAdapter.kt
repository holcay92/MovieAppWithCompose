package com.example.movieapp.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.databinding.MovieItemPopularBinding
import com.example.movieapp.model.popularMovie.ResultPopular
import com.example.movieapp.room.FavoriteMovie

class FavoriteMovieAdapter(private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteViewHolder>() {

    private var favMovieList = ArrayList<FavoriteMovie>()

     class FavoriteViewHolder(bindingItem: MovieItemPopularBinding) :
        RecyclerView.ViewHolder(bindingItem.root) {
        fun bind(popularMovie: FavoriteMovie) {
            val bindingItem = MovieItemPopularBinding.bind(itemView)
            Log.d("TAG_X", "fav adapter bind in the adapter popularMovie: $popularMovie")
            bindingItem.apply {
                bindingItem.movieTitle.text = popularMovie.title
                bindingItem.btnAddFav.setImageResource(R.drawable.add_fav_filled_icon)
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500${popularMovie.poster_path}").centerCrop()
                    .into(bindingItem.movieImage)
                Log.d("TAG_X", "fav adapter bind in the adapter popularMovie.id : ${popularMovie.id}")

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FavoriteViewHolder {

        val itemView =
            MovieItemPopularBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavoriteMovieAdapter.FavoriteViewHolder, position: Int) {
        holder.bind(favMovieList[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(favMovieList[position])
        }
        Log.d("TAG_X", "fav adapter onBindViewHolder: ${favMovieList[position]}")

    }

    override fun getItemCount()= favMovieList.size

    fun updateList(list: List<FavoriteMovie>?) {
        Log.d("TAG_X", "fav adapter updateList in the adapter list: $favMovieList")
        favMovieList.clear()

        favMovieList.addAll(list ?: emptyList())
        Log.d("TAG_X", "fav adapter updateList in the adapter list after: $favMovieList")
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(movie: FavoriteMovie)
    }
}
