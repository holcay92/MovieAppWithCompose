package com.example.movieapp.model.movieImages

import com.google.gson.annotations.SerializedName

data class MovieImages(
    val backdrops: List<Backdrop>,
    val id: Int,
    val logos: List<Logo>,
    @SerializedName("posters") val posters: List<Poster>?,
)
