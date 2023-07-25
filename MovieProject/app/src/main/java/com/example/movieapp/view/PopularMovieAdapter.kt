package com.example.movieapp.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.databinding.MovieItemBinding
import com.example.movieapp.model.popularMovie.ResultPopular

class PopularMovieAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<PopularMovieAdapter.MovieViewHolder>() {
    private var movieList = ArrayList<ResultPopular>()

    class MovieViewHolder(bindingItem: MovieItemBinding) :
        RecyclerView.ViewHolder(bindingItem.root) {
        fun bind(popularMovie: ResultPopular) {
            val bindingItem = MovieItemBinding.bind(itemView)
            bindingItem.apply {
                movieTitle?.text = popularMovie.title
                Log.d("TAG_X", "bind in the adapter popularMovie.title : ${popularMovie.title}")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView =
            MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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
        movieList.clear()
        movieList.addAll(list ?: emptyList())
        Log.d("TAG_X", "Adapter Pokemon updateList in the adapter pokemonlist: $movieList")
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(movie: ResultPopular)
    }
}
