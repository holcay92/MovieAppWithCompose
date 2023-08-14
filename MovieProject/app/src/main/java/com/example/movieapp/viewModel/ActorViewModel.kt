package com.example.movieapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.model.actor.Actor
import com.example.movieapp.service.MovieApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ActorViewModel @Inject constructor(
    private val movieApiService: MovieApiService,
) : ViewModel() {

    val actorResponse = MutableLiveData<Actor?>()

    fun getActorDetail(id: Int) {
        val call = movieApiService.getActorDetails(id)
        call.enqueue(object : Callback<Actor?> {
            override fun onResponse(
                call: Call<Actor?>,
                response: Response<Actor?>,
            ) {
                if (response.isSuccessful) {
                    actorResponse.value = response.body()
                }
            }

            override fun onFailure(call: Call<Actor?>, t: Throwable) {
                actorResponse.postValue(null) // todo error check
            }
        })
    }
}
