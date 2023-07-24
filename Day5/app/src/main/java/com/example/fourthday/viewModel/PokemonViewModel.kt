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
class PokemonViewModel @Inject constructor(private val pokeApiService: PokeApiService) :
    ViewModel() {
    var pokemonResponse = MutableLiveData<PokemonResponse?>()
    var pokemonDetailResponse = MutableLiveData<PokemonDetail?>()

    init {
        fetchNextPokemonList()
    }

    private var offset = 0
    private val LIMIT = 20

    private fun fetchNextPokemonList() {
        val call = pokeApiService.getPokemonList(LIMIT, offset)
        call.enqueue(object : Callback<PokemonResponse?> {

            override fun onResponse(
                call: Call<PokemonResponse?>,
                response: Response<PokemonResponse?>,
            ) {
                if (response.isSuccessful) {
                    pokemonResponse.value = (response.body())

                    val callDetail = pokeApiService.getPokemonDetail(1)

                    callDetail.enqueue(object :
                        Callback<PokemonDetail?> { // this is for detail fragment
                        override fun onResponse(
                            call: Call<PokemonDetail?>,
                            responseDetail: Response<PokemonDetail?>,
                        ) {
                            if (responseDetail.isSuccessful) {
                                pokemonDetailResponse.value = (responseDetail.body())
                                getPokemonDetail()
                            }
                        }

                        override fun onFailure(call: Call<PokemonDetail?>, t: Throwable) {
                        }
                    })
                }
            }

            override fun onFailure(call: Call<PokemonResponse?>, t: Throwable) {
                pokemonResponse.postValue(null)
            }
        })
    }

    private fun getPokemonDetail() {
        for (i in offset..offset + LIMIT) {
            val callDetail = pokeApiService.getPokemonDetail(i)
            callDetail.enqueue(object : Callback<PokemonDetail?> {
                override fun onResponse(
                    call: Call<PokemonDetail?>,
                    response: Response<PokemonDetail?>,
                ) {
                    if (response.isSuccessful) {
                        pokemonDetailResponse.value = (response.body())
                    }
                }

                override fun onFailure(call: Call<PokemonDetail?>, t: Throwable) {
                    Log.d("TAG_X", "onFailureDetail: ${t.message}")
                }
            })
        }
        Log.d("TAG_X", "getPokemonDetail test end of loop: ${pokemonDetailResponse.value}")
    }

    fun loadNextSet() {
        offset += LIMIT
        fetchNextPokemonList()
        Log.d(
            "TAG_X",
            "PokemonViewModel fetchNextPokemonList init test next btn: ${pokemonDetailResponse.value}",
        )
    }

    fun loadPreviousSet() {
        if (offset > 0) {
            offset -= LIMIT
            fetchNextPokemonList()
        } else {
            return
        }
    }
}
