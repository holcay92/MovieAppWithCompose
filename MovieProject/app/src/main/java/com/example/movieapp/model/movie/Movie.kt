package com.example.movieapp.model.movie

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("results") val results: List<MovieResult>?,
)
