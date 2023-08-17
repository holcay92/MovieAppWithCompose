package com.example.movieapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.R
import com.example.movieapp.model.review.Review
import com.example.movieapp.model.review.ReviewResult
import com.example.movieapp.service.MovieApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DetailReviewViewModel @Inject constructor(private val apiService: MovieApiService) :
    ViewModel() {

    val reviewList = MutableStateFlow<List<ReviewResult>?>(null)
    val errorMessageMovieReview = MutableStateFlow<String?>(null)

    fun getReview(id: Int) {
        viewModelScope.launch {
            val call = apiService.getMovieReviews(id)
            call.enqueue(object : Callback<Review?> {
                override fun onResponse(call: Call<Review?>, response: Response<Review?>) {
                    if (response.isSuccessful) {
                        reviewList.value = response.body()?.results
                    }
                }

                override fun onFailure(call: Call<Review?>, t: Throwable) {
                    errorMessageMovieReview.value =
                        R.string.error_message_movie_review.toString()
                }
            })
        }
    }
}
