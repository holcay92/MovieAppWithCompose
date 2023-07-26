package com.example.movieapp.model.movieImages

import com.google.gson.annotations.SerializedName

data class Poster(
    val aspect_ratio: Double,
    @SerializedName("file_path") val file_path: String?,
    val height: Int,
    val iso_639_1: String,
    val vote_average: Double,
    val vote_count: Int,
    val width: Int,
)
