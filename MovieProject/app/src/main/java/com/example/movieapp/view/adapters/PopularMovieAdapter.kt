package com.example.movieapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.movieapp.R
import com.example.movieapp.databinding.MovieItemPopularBinding
import com.example.movieapp.databinding.MovieItemPopularGridViewBinding
import com.example.movieapp.model.popularMovie.ResultPopular
import com.example.movieapp.room.FavoriteMovie

class PopularMovieAdapter(
    private val listener: OnItemClickListener,
    private val favStatusChangeListener: OnFavoriteStatusChangeListener,
) :
    RecyclerView.Adapter<PopularMovieAdapter.MovieViewHolder>() {
    private var movieList = ArrayList<ResultPopular>()
    private var viewType = ViewType.LIST

    enum class ViewType {
        LIST, GRID
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setViewType(viewType: ViewType) {
        this.viewType = viewType
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(bindingItem: ViewBinding) :
        RecyclerView.ViewHolder(bindingItem.root) {
        init {
            if (bindingItem is MovieItemPopularBinding) {
                bindingItem.btnAddFav.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val movie = movieList[position]
                        movie.isFavorite = !movie.isFavorite
                        favStatusChangeListener.onFavoriteStatusChanged(movie)
                        notifyItemChanged(position)
                    }
                }
            } else if (bindingItem is MovieItemPopularGridViewBinding) {
                bindingItem.btnAddFav.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val movie = movieList[position]
                        movie.isFavorite = !movie.isFavorite
                        favStatusChangeListener.onFavoriteStatusChanged(movie)
                        notifyItemChanged(position)
                    }
                }
            }
        }

        fun bind(popularMovie: ResultPopular) {
            if (viewType == ViewType.LIST) {
                val bindingItem = MovieItemPopularBinding.bind(itemView)
                bindingItem.apply {
                    movieTitle.text = popularMovie.title
                    if (popularMovie.isFavorite) {
                        btnAddFav.setImageResource(R.drawable.add_fav_filled_icon)
                    } else {
                        btnAddFav.setImageResource(R.drawable.add_fav_empty_icon)
                    }

                    movieVote.setBackgroundResource(
                        when (updateVoteStyle(popularMovie.vote_average)) {
                            "green" -> R.drawable.good
                            "yellow" -> R.drawable.intermediate
                            else -> R.drawable.normal
                        },
                    )
                    Glide.with(itemView.context)
                        .load("https://image.tmdb.org/t/p/w500${popularMovie.poster_path}")
                        .centerCrop().transform(CenterCrop(), RoundedCorners(20))
                        .into(movieImage)
                }
            } else {
                val bindingItem = MovieItemPopularGridViewBinding.bind(itemView)
                bindingItem.apply {
                    if (popularMovie.isFavorite) {
                        btnAddFav.setImageResource(R.drawable.add_fav_filled_icon)
                    } else {
                        btnAddFav.setImageResource(R.drawable.add_fav_empty_icon)
                    }
                    movieVote.setBackgroundResource(
                        when (updateVoteStyle(popularMovie.vote_average)) {
                            "green" -> R.drawable.good
                            "yellow" -> R.drawable.intermediate
                            else -> R.drawable.normal
                        },
                    )
                    Glide.with(itemView.context)
                        .load("https://image.tmdb.org/t/p/w500${popularMovie.poster_path}")
                        .centerCrop()
                        .into(movieImage)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return if (viewType == ViewType.LIST.ordinal) {
            val itemView =
                MovieItemPopularBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MovieViewHolder(itemView)
        } else {
            val itemView =
                MovieItemPopularGridViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            MovieViewHolder(itemView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType.ordinal
    }

    override fun getItemCount() = movieList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieList[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(movieList[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<ResultPopular>?) {
        movieList.addAll(list ?: emptyList())
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(movie: ResultPopular)
    }

    fun updateVoteStyle(vote: Double): String {
        return if (vote >= 7.0) {
            "green"
        } else if (vote >= 5.0) {
            "yellow"
        } else {
            "red"
        }
    }

    interface OnFavoriteStatusChangeListener {
        fun onFavoriteStatusChanged(movie: ResultPopular)
    }

    fun updateFavoriteStatus(favoriteMovies: List<FavoriteMovie>) {
        movieList.forEach { movie ->
            val isFav = favoriteMovies.any { it.id == movie.id }
            movie.isFavorite = isFav
        }
        notifyDataSetChanged()
    }
}
