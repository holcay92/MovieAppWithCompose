package com.example.movieapp.view.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.MovieItemPopularBinding
import com.example.movieapp.room.FavoriteMovie

class FavoriteListAdapter: RecyclerView.Adapter<FavoriteListAdapter.FavoriteViewHolder>()  {

    inner class FavoriteViewHolder(bindingItem: MovieItemPopularBinding) :
    RecyclerView.ViewHolder(bindingItem.root) {
        fun bind(popularMovie: FavoriteMovie) {
            val bindingItem = MovieItemPopularBinding.bind(itemView)
            bindingItem.apply {

            }
        }}


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteListAdapter.FavoriteViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: FavoriteListAdapter.FavoriteViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}