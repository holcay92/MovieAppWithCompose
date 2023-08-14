package com.example.movieapp.model.review

import com.google.gson.annotations.SerializedName

data class ReviewResult(
    @SerializedName("author") val author: String,
    @SerializedName("content") val content: String,
    @SerializedName("id") val id: String,
    @SerializedName("author_details") val authorDetails: AuthorDetails,
)
