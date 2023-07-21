package com.example.fourthday

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fourthday.model.PokemonResponse
import com.example.fourthday.service.RetrofitInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@HiltViewModel
class PokemonViewModel : ViewModel() {
    // not a list but a single PokemonResponse object
     var pokemonResponse = MutableLiveData<PokemonResponse?>()
   init {
         fetchNextPokemonList()
   }

    private var offset = 0
    private val LIMIT = 20

    fun fetchNextPokemonList() {
        val call = RetrofitInstance.pokeApiService().getPokemonList(offset, LIMIT)

        call.enqueue(object : Callback<PokemonResponse?> {

            override fun onResponse(
                call: Call<PokemonResponse?>,
                response: Response<PokemonResponse?>
            ) {
                if (response.isSuccessful) {
                    pokemonResponse.postValue(response.body())
                    Log.d("TAG_X", "onResponse: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<PokemonResponse?>, t: Throwable) {
                pokemonResponse.postValue(null)
                Log.d("TAG_X", "onFailure: ${t.message}")


            }
        })
    }

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
