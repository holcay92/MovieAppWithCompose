package com.example.movieapp.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.MovieItemBinding
import com.example.movieapp.model.topRated.ResultTopRated

class TopRatedMovieAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<TopRatedMovieAdapter.TopRatedMovieViewHolder>() {
    private var tRMovieList = ArrayList<ResultTopRated>()

    class TopRatedMovieViewHolder(bindingItem: MovieItemBinding) :
        RecyclerView.ViewHolder(bindingItem.root) {
        fun bind(tRMovie: ResultTopRated) {
            val bindingItem = MovieItemBinding.bind(itemView)
            bindingItem.apply {
                movieTitle?.text = tRMovie.title
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500${tRMovie.poster_path}").centerCrop()
                    .into(movieImage!!)
                Log.d("TAG_X", "bind in the adapter popularMovie.title : ${tRMovie.title}")
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TopRatedMovieViewHolder {
        val itemView =
            MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TopRatedMovieViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: TopRatedMovieViewHolder,
        position: Int,
    ) {
        holder.bind(tRMovieList[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(tRMovieList[position])
        }
    }

    override fun getItemCount() = tRMovieList.size

    fun updateList(list: List<ResultTopRated>?) {
        tRMovieList.clear()
        tRMovieList.addAll(list ?: emptyList())
        Log.d("TAG_X", "Adapter Pokemon updateList in the adapter pokemonlist: $tRMovieList")
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(movie: ResultTopRated)
    }
}
