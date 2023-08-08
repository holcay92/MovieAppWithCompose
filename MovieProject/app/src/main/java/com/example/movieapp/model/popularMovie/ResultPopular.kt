package com.example.movieapp.model.popularMovie

import com.google.gson.annotations.SerializedName

data class ResultPopular(
    @SerializedName("adult") val adult: Boolean?,
    @SerializedName("id") val id: Int?,
    @SerializedName("original_title") val originalTitle: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("popularity")val popularity: Double?,
    @SerializedName("poster_path")val posterPath: String?,
    @SerializedName("release_date")val releaseDate: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("video") val video: Boolean?,
    @SerializedName("vote_average")val voteAverage: Double?,
    @SerializedName("vote_count")val voteCount: Int?,
    @SerializedName("isFavorite") var isFavorite: Boolean = false, // custom added to check if favorite or not
)
