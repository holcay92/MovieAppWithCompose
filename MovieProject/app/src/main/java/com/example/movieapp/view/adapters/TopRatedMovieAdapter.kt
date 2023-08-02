package com.example.movieapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.databinding.MovieItemGridBinding
import com.example.movieapp.model.topRated.ResultTopRated

class TopRatedMovieAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<TopRatedMovieAdapter.TopRatedMovieViewHolder>() {
    private var tRMovieList = ArrayList<ResultTopRated>()

    class TopRatedMovieViewHolder(bindingItem: MovieItemGridBinding) :
        RecyclerView.ViewHolder(bindingItem.root) {
        fun bind(tRMovie: ResultTopRated) {
            val bindingItem = MovieItemGridBinding.bind(itemView)
            bindingItem.apply {
                // movieTitle.text = tRMovie.title
                if (tRMovie.isFavorite) {
                    btnAddFav.setImageResource(R.drawable.add_fav_filled_icon)
                } else {
                    btnAddFav.setImageResource(R.drawable.add_fav_empty_icon)
                }
                tvMovieRating.text = tRMovie.vote_average.toString()
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500${tRMovie.poster_path}").centerCrop()
                    .into(movieImage)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TopRatedMovieViewHolder {
        val itemView =
            MovieItemGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<ResultTopRated>?) {
        tRMovieList.clear()
        tRMovieList.addAll(list ?: emptyList())
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(movie: ResultTopRated)
    }
}
