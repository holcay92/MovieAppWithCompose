package com.example.movieapp.model.review

import com.google.gson.annotations.SerializedName

data class AuthorDetails(
    val avatar_path: String,
    @SerializedName("name") val name: String,
    val rating: Int,
    val username: String,
)
