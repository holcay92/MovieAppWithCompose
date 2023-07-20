package com.example.fourthday

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fourthday.model.Pokemon
import com.example.fourthday.service.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PokemonViewModel : ViewModel() {

    // LiveData to hold the list of Pokémon data
    private val _pokemonListLiveData = MutableLiveData<List<Pokemon>>()


    // Function to fetch Pokémon data from the API and update the LiveData

    fun fetchPokemonData() {
        val apiService = RetrofitInstance.pokeApiService

        // (offset: 0, limit: 20)
        apiService.getPokemon(offset = 0, limit = 20).enqueue(object : Callback<List<Pokemon>> {

            override fun onResponse(call: Call<List<Pokemon>>, response: Response<List<Pokemon>>) {
                if (response.isSuccessful) {
                    Log.d("PokemonViewModel", "Response successful")
                    val pokemonResponse = response.body()
                    _pokemonListLiveData.postValue(pokemonResponse!!)
                } else {
                    // Handle error response
                    Log.e(
                        "PokemonViewModel",
                        "Error fetching Pokémon data ${response.code()} message: ${response.message()} "
                    )
                }
            }

            override fun onFailure(call: Call<List<Pokemon>>, t: Throwable) {
              Log.e("PokemonViewModel", "Error fetching Pokémon data", t)
            }
        })

    }
}
