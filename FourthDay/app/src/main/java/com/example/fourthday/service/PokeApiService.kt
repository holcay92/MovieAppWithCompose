package com.example.fourthday.service

import com.example.fourthday.model.Pokemon
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PokeApiService {

    @GET("?offset={a}&limit={b}")
    fun getPokemon(@Query("a") offset: Int, @Query("b") limit: Int): Call<List<Pokemon>>
}