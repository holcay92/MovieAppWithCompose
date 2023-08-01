package com.example.movieapp.model.popularMovie

import com.google.gson.annotations.SerializedName

data class ResultPopular(
    val adult: Boolean,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    @SerializedName("isFavorite") var isFavorite: Boolean = false, // custom added to check if favorite or not
)
