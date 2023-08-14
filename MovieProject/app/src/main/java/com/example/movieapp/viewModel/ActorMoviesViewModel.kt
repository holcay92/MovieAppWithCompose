package com.example.movieapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.model.actorMovies.ActorMovies
import com.example.movieapp.model.actorMovies.ActorMoviesCrew
import com.example.movieapp.service.MovieApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ActorMoviesViewModel @Inject constructor(private val movieApiService: MovieApiService) :
    ViewModel() {

    val actorMovies = MutableLiveData<List<ActorMoviesCrew>?>()

    fun getActorMovies(id: Int) {
        val call = movieApiService.getActorMovies(id)
        call.enqueue(object : Callback<ActorMovies> {
            override fun onResponse(
                call: Call<ActorMovies?>,
                response: Response<ActorMovies?>,
            ) {
                if (response.isSuccessful) {
                    actorMovies.value = response.body()?.crew
                }
            }

            override fun onFailure(call: Call<ActorMovies?>, t: Throwable) {
                actorMovies.postValue(null) // todo error check
            }
        })
    }

}
