package com.example.movieapp.model.movieImages

import com.google.gson.annotations.SerializedName

data class MovieImages(
    @SerializedName("id") val id: Int,
    @SerializedName("posters") val posters: List<Poster>?,
)
