package com.example.movieapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.databinding.ReviewItemBinding
import com.example.movieapp.model.review.ReviewResult

class DetailReviewAdapter : RecyclerView.Adapter<DetailReviewAdapter.DetailReviewViewHolder>() {

    private val reviewList = ArrayList<ReviewResult>()

    inner class DetailReviewViewHolder(bindingItem: ReviewItemBinding) :
        RecyclerView.ViewHolder(bindingItem.root) {

        fun bind(review: ReviewResult) {
            val bindingItem = ReviewItemBinding.bind(itemView)
            bindingItem.apply {
                reviewAuthor.text = review.author
                reviewContent.text = review.content
                reviewVote.text = review.author_details.rating.toString()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DetailReviewAdapter.DetailReviewViewHolder {
        val itemView = ReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailReviewViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: DetailReviewAdapter.DetailReviewViewHolder,
        position: Int,
    ) {
        holder.bind(reviewList[position])
    }

    override fun getItemCount() = reviewList.size

    fun updateList(newList: List<ReviewResult>) {
        reviewList.clear()
        reviewList.addAll(newList)
        notifyDataSetChanged()
    }
}
