package com.example.fourthday.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fourthday.model.PokemonDetail
import com.example.fourthday.model.PokemonResponse
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
    private val id: Int = 1
    var pokemonDetailResponse = MutableLiveData<PokemonDetail?>()


    init {
        fetchPokemonDetail()
        Log.d("TAG_X", "PokemonViewModel fetchNextPokemonList init: ${pokemonDetailResponse.value}")
    }

    private fun fetchPokemonDetail() {
        val call = pokeApiService.getPokemonDetail(id)

        call.enqueue(object : Callback<PokemonDetail?> {
            override fun onResponse(
                call: Call<PokemonDetail?>,
                response: Response<PokemonDetail?>
            ) {
                if (response.isSuccessful) {
                    pokemonDetailResponse.value = (response.body())
                    Log.d("TAG_X", "onResponse11: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<PokemonDetail?>, t: Throwable) {
                pokemonDetailResponse.postValue(null)
                Log.d("TAG_X", "onFailure: ${t.message}")

            }
        })
    }

}