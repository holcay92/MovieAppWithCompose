package com.example.movieapp.model.topRated

import com.google.gson.annotations.SerializedName

data class ResultTopRated(
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("genre_ids") val genre_ids: List<Int>,
    @SerializedName("id") val id: Int,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val poster_path: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("title") val title: String,
    @SerializedName("video") val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("isFavorite") var isFavorite: Boolean = false, // custom added to check if favorite or not
)
