package com.example.movieapp.model.movieDetail

import com.google.gson.annotations.SerializedName

data class MovieDetail(
    @SerializedName("adult") val adult: Boolean?,
    @SerializedName("budget") val budget: Int?,
    @SerializedName("genres") val genres: List<Genre>?,
    val id: Int,
    @SerializedName("original_title") val original_title: String?,
    @SerializedName("overview") val overview: String?,
    val popularity: Double,
    val poster_path: String,
    @SerializedName("release_date") val release_date: String?,
    val revenue: Int,
    @SerializedName("title") val title: String?,
    val video: Boolean,
    @SerializedName("vote_average") val vote_average: Double?,
    val vote_count: Int,
)
