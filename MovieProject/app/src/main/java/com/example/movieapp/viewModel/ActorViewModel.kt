package com.example.movieapp.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.model.actor.Actor
import com.example.movieapp.service.MovieApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActorViewModel @Inject constructor(
    private val movieApiService: MovieApiService,
) : ViewModel() {

    val actorResponse = MutableLiveData<Actor?>()

    fun getActorDetail(id: Int) {
        val call = movieApiService.getActorDetails(id)
        call.enqueue(object : retrofit2.Callback<Actor?> {
            override fun onResponse(
                call: retrofit2.Call<Actor?>,
                response: retrofit2.Response<Actor?>,
            ) {
                if (response.isSuccessful) {
                    actorResponse.value = response.body()
                    Log.d("TAGX", "viewmodel onResponse: ${response.body()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<Actor?>, t: Throwable) {
                actorResponse.postValue(null)
            }
        })
    }


}
