package com.example.movieapp.model.movieImages

import com.google.gson.annotations.SerializedName

data class Poster(
    @SerializedName("file_path") val filePath: String?,
)
