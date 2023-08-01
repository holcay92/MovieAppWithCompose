package com.example.movieapp.model.review

import com.google.gson.annotations.SerializedName

data class ReviewResult(
    @SerializedName("author") val author: String,
    val author_details: AuthorDetails,
    @SerializedName("content") val content: String,
    val created_at: String,
    val id: String,
    val updated_at: String,
    val url: String,
)
