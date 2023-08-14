package com.example.movieapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.movieapp.R
import com.example.movieapp.databinding.MovieItemGridBinding
import com.example.movieapp.model.movie.MovieResult
import com.example.movieapp.room.FavoriteMovie

class NowPlayingMovieAdapter(
    private val listener: OnItemClickListener,
    private val favStatusChangeListener: OnFavoriteStatusChangeListener,
) :
    RecyclerView.Adapter<NowPlayingMovieAdapter.TrendingMovieViewHolder>() {
    private var trendingMovieList = ArrayList<MovieResult>()

    inner class TrendingMovieViewHolder(bindingItem: MovieItemGridBinding) :
        RecyclerView.ViewHolder(bindingItem.root) {
        init {
            val btnAddFav = bindingItem.favButton

            btnAddFav.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val movie = trendingMovieList[position]
                    movie.isFavorite = !movie.isFavorite
                    favStatusChangeListener.onFavoriteStatusChanged3(movie)
                    notifyItemChanged(position)
                }
            }
        }

        fun bind(trendingMovie: MovieResult) {
            val bindingItem = MovieItemGridBinding.bind(itemView)
            bindingItem.apply {
                if (trendingMovie.isFavorite) {
                    favButton.setImageResource(R.drawable.add_fav_filled_icon_top_rated)
                } else {
                    favButton.setImageResource(R.drawable.add_fav_empty_icon_top_rated)
                }
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500${trendingMovie.posterPath}").fitCenter()
                    .transform(CenterCrop(), RoundedCorners(50))
                    .into(movieImage)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TrendingMovieViewHolder {
        val itemView =
            MovieItemGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TrendingMovieViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: TrendingMovieViewHolder,
        position: Int,
    ) {
        holder.bind(trendingMovieList[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(trendingMovieList[position])
        }
    }

    override fun getItemCount() = trendingMovieList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<MovieResult>?) {
        trendingMovieList.clear()
        trendingMovieList.addAll(list ?: emptyList())
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(movie: MovieResult)
    }

    interface OnFavoriteStatusChangeListener {
        fun onFavoriteStatusChanged3(movie: MovieResult)
    }

    fun updateFavoriteStatus(favoriteMovies: List<FavoriteMovie>) {
        trendingMovieList.forEach { movie ->
            val isFav = favoriteMovies.any { it.id == movie.id }
            movie.isFavorite = isFav
        }
        notifyDataSetChanged()
    }
}
