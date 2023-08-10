package com.example.movieapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.model.credits.Credits
import com.example.movieapp.service.MovieApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CreditsViewModel @Inject constructor(private val movieApiService: MovieApiService) :
    ViewModel() {
    var creditsResponse = MutableLiveData<Credits?>()

    fun getMovieCredits(id: Int) {
        movieApiService.getMovieCredits(id)?.enqueue(
            object : Callback<Credits?> {
                override fun onResponse(
                    call: Call<Credits?>,
                    response: Response<Credits?>,
                ) {
                    if (response.isSuccessful) {
                        creditsResponse.value = response.body()
                    }
                }

                override fun onFailure(call: Call<Credits?>, t: Throwable) {
                    creditsResponse.postValue(null)
                }
            },
        )
    }
}
