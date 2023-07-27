package com.example.movieapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.MovieItemPopularBinding
import com.example.movieapp.model.popularMovie.ResultPopular

class PopularMovieAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<PopularMovieAdapter.MovieViewHolder>() {
    private var movieList = ArrayList<ResultPopular>()

    class MovieViewHolder(bindingItem: MovieItemPopularBinding) :
        RecyclerView.ViewHolder(bindingItem.root) {
        fun bind(popularMovie: ResultPopular) {
            val bindingItem = MovieItemPopularBinding.bind(itemView)
            bindingItem.apply {
                movieTitle?.text = popularMovie.title
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500${popularMovie.poster_path}").centerCrop()
                    .into(movieImage!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView =
            MovieItemPopularBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MovieViewHolder(itemView)
    }

    override fun getItemCount() = movieList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieList[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(movieList[position])
        }
    }

    fun updateList(list: List<ResultPopular>?) {
        // movieList.clear()
        movieList.addAll(list ?: emptyList())
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(movie: ResultPopular)
    }
}