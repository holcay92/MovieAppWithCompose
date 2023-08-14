package com.example.movieapp.view.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.ActorMovieItemBinding
import com.example.movieapp.model.actorMovies.ActorMoviesCrew

class ActorMoviesAdapter(private val onActorMovieClickListener: OnActorMovieClickListener) :
    RecyclerView.Adapter<ActorMoviesAdapter.ActorMoviesViewHolder>() {

    private val actorMovies = mutableListOf<ActorMoviesCrew>()

    inner class ActorMoviesViewHolder(itemView: ActorMovieItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        fun bind(actorMovie: ActorMoviesCrew?) {
            val bindingItem = ActorMovieItemBinding.bind(itemView)
            if (actorMovie?.posterPath == null) {
                bindingItem.actorMovieImage.setImageResource(com.example.movieapp.R.drawable.blank_movie_image)
            } else {
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500${actorMovie.posterPath}")
                    .into(bindingItem.actorMovieImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorMoviesViewHolder {
        val itemView = ActorMovieItemBinding.inflate(
            android.view.LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return ActorMoviesViewHolder(itemView)
    }

    override fun getItemCount() = actorMovies.size

    override fun onBindViewHolder(holder: ActorMoviesViewHolder, position: Int) {
        holder.bind(actorMovies[position])
        holder.itemView.setOnClickListener {
            onActorMovieClickListener.onActorMovieClick(actorMovies[position])
        }
    }

    interface OnActorMovieClickListener {
        fun onActorMovieClick(actorMovie: ActorMoviesCrew)
    }

    fun updateActorMovies(newActorMovies: List<ActorMoviesCrew>) {
        actorMovies.clear()
        actorMovies.addAll(newActorMovies)
        notifyDataSetChanged()
    }
}
