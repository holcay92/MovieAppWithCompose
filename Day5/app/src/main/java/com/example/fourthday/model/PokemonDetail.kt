package com.example.fourthday.model

import com.google.gson.annotations.SerializedName

data class PokemonDetail(
    @SerializedName("height") val height: Int?,
    @SerializedName("moves") val moves: List<Move>?,
    @SerializedName("name") val name: String?,
    @SerializedName("sprites") val sprites: Sprites?,
    @SerializedName("weight") val weight: Int?,
)
