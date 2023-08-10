package com.example.movieapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.ActorItemBinding
import com.example.movieapp.model.credits.Cast

class ActorAdapter(private val onClickListener: OnItemClickListener) :
    RecyclerView.Adapter<ActorAdapter.ActorViewHolder>() {

    private var actorList = listOf<Cast>()

    inner class ActorViewHolder(bindingItem: ActorItemBinding) :
        RecyclerView.ViewHolder(bindingItem.root) {

        fun bind(actor: Cast) {
            val bindingItem = ActorItemBinding.bind(itemView)
            bindingItem.apply {
                actorName.text = actor.name
                Glide.with(actorImage.context)
                    .load("https://image.tmdb.org/t/p/w500${actor.profile_path}")
                    .into(actorImage)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ActorAdapter.ActorViewHolder {
        val itemView = ActorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActorViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ActorAdapter.ActorViewHolder, position: Int) {
        holder.bind(actorList[position])
        holder.itemView.setOnClickListener {
            onClickListener.onItemClick(actorList[position])
        }
    }

    override fun getItemCount() = actorList.size

    interface OnItemClickListener {
        fun onItemClick(actor: Cast)
    }

    fun updateList(newList: List<Cast>) {
        actorList = newList
        notifyDataSetChanged()
    }
}
