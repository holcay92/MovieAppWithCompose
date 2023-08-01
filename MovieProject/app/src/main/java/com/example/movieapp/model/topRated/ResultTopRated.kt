package com.example.movieapp.model.topRated

import com.google.gson.annotations.SerializedName

data class ResultTopRated(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    @SerializedName("id") val id: Int,
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
