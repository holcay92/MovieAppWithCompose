package com.example.movieapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.databinding.MovieItemBinding
import com.example.movieapp.model.MovieSearchResponse

class MovieAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private var movieList = ArrayList<MovieSearchResponse>()

    class MovieViewHolder(bindingItem: MovieItemBinding) :
        RecyclerView.ViewHolder(bindingItem.root) {
        fun bind(movie: MovieSearchResponse) {
            val bindingItem = MovieItemBinding.bind(itemView)
            bindingItem.apply {
                //todo
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

    interface OnItemClickListener {
        fun onItemClick(movie: MovieSearchResponse)
    }
}
