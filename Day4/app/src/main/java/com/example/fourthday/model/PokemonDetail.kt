package com.example.fourthday.model

data class PokemonDetail(
    val height: Int,
    val moves: List<Move>,
    val name: String,
    val sprites: Sprites,
    val weight: Int
)