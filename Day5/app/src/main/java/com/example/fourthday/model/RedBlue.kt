package com.example.fourthday.model

import com.google.gson.annotations.SerializedName

data class RedBlue(
    @SerializedName("front_transparent") val front_transparent: String? //show only this image
)