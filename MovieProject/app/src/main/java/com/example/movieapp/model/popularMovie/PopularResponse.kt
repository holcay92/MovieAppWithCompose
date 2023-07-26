package com.example.movieapp.model.popularMovie

import com.google.gson.annotations.SerializedName

data class PopularResponse(
    @SerializedName("results") val results: List<ResultPopular>?,
)
