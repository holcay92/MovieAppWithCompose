package com.example.fourthday

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fourthday.model.Pokemon
import com.example.fourthday.model.PokemonResponse
import com.example.fourthday.service.PokeApiService
import com.example.fourthday.service.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonViewModel : ViewModel() {

    private val apiService = RetrofitInstance.pokeApiService()

    private val _pokemonList = MutableLiveData<PokemonResponse?>()

    val pokemonList: MutableLiveData<PokemonResponse?>
        get() = _pokemonList

    private var offset = 0
    private val LIMIT = 20

    fun fetchNextPokemonList() {
        val call = apiService.getPokemonList(LIMIT, offset)

        call.enqueue(object : Callback<PokemonResponse?> {

            override fun onResponse(
                call: Call<PokemonResponse?>,
                response: Response<PokemonResponse?>
            ) {
                if (response.isSuccessful) {
                    _pokemonList.postValue(response.body())
                    Log.d("TAG_X", "onResponse: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<PokemonResponse?>, t: Throwable) {
                _pokemonList.postValue(null)
                Log.d("TAG_X", "onFailure: ${t.message}")


            }
        })
    }

    // Method to load the next set of Pokémon with a new offset
    fun loadNextSet() {
        offset += LIMIT
        fetchNextPokemonList()
    }

    // Method to go back to the previous set of Pokémon with a new offset
    fun loadPreviousSet() {
        offset = offset.coerceAtLeast(LIMIT)
        fetchNextPokemonList()
    }
}
