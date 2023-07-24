package com.example.fourthday.model

import com.google.gson.annotations.SerializedName

data class Sprites(
    @SerializedName("versions") val versions: Versions?
)