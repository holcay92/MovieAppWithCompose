package com.example.movieapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.databinding.SearchItemBinding
import com.example.movieapp.model.movieSearchResponse.SearchResult

class SearchListAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<SearchListAdapter.SearchViewHolder>() {

    private var searchList = MutableLiveData<List<SearchResult>>()

    class SearchViewHolder(bindingItem: SearchItemBinding) :
        RecyclerView.ViewHolder(bindingItem.root) {

        fun bind(movie: SearchResult) {
            val bindingItem = SearchItemBinding.bind(itemView)
            bindingItem.apply {
                movieTitle.text = movie.title
                if (movie.isFavorite) {
                    isFavMovie.setImageResource(R.drawable.add_fav_filled_icon)
                } else {
                    isFavMovie.setImageResource(R.drawable.add_fav_empty_icon)
                }

                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500${movie.posterPath}").centerCrop()
                    .into(moviePoster)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchViewHolder {
        val itemView =
            SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(searchList.value!![position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(searchList.value!![position])
        }
    }

    override fun getItemCount() = searchList.value?.size ?: 0

    interface OnItemClickListener {
        fun onItemClick(movie: SearchResult)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<SearchResult>?) {
        searchList.value = list ?: emptyList()
        notifyDataSetChanged()
    }
}
