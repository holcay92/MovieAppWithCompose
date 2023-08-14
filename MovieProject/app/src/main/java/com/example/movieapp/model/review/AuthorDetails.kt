package com.example.movieapp.model.review

import com.google.gson.annotations.SerializedName

data class AuthorDetails(
    @SerializedName("name") val name: String,
    @SerializedName("rating") val rating: Int,
)
