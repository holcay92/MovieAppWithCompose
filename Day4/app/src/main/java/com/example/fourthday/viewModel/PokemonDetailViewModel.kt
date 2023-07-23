package com.example.fourthday.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fourthday.model.PokemonDetail
import com.example.fourthday.service.PokeApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val pokeApiService: PokeApiService,

) : ViewModel() {
   // private val id: Int = 1
    var pokemonDetailResponse =MutableLiveData<PokemonDetail?>()


    init {
        //fetchPokemonDetail(id)

        Log.d("TAG_X", "PokemonDetailViewModel fetchNextPokemonList init: ${pokemonDetailResponse}")
    }

    fun fetchPokemonDetail(id:Int) {
        val call = pokeApiService.getPokemonDetail(id)
        Log.d("TAG_X", "PokemonDetailViewModel fetchPokemonDetail call: : $call")

        call.enqueue(object : Callback<PokemonDetail?> {
            override fun onResponse(
                call: Call<PokemonDetail?>,
                response: Response<PokemonDetail?>
            ) {
                Log.d("TAG_X", "PokemonDetailViewModel onResponse: $response")
                if (response.isSuccessful) {
                    pokemonDetailResponse.value = response.body()
                    Log.d("TAG_X", "PokemonDetailViewModel onResponse11: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<PokemonDetail?>, t: Throwable) {
                pokemonDetailResponse.postValue(null)
                Log.d("TAG_X", "PokemonDetailViewModel onFailure: ${t.message}")

            }
        })
    }
}