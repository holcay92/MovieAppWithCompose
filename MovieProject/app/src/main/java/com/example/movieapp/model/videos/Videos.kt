package com.example.movieapp.model.videos

import com.google.gson.annotations.SerializedName

data class Videos(
    @SerializedName("id") val id: Int,
    @SerializedName("results") val results: List<VideoResult>,
)
