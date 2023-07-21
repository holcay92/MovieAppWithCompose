package com.example.fourthday.service

import com.example.fourthday.model.Pokemon
import com.example.fourthday.model.PokemonResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon/")
    fun getPokemonList(@Query("limit") limit: Int, @Query("offset") offset: Int): Call<PokemonResponse>
}
