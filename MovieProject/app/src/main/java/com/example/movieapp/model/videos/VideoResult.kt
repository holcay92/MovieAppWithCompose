package com.example.movieapp.model.videos

import com.google.gson.annotations.SerializedName

data class VideoResult(
    @SerializedName("id") val id: String,
    @SerializedName("key") val key: String,
)
