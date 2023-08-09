package com.example.movieapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.R
import com.example.movieapp.model.review.Review
import com.example.movieapp.model.review.ReviewResult
import com.example.movieapp.service.MovieApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import javax.inject.Inject

@HiltViewModel
class DetailReviewViewModel @Inject constructor(private val apiService: MovieApiService) :
    ViewModel() {

    val reviewList = MutableLiveData<List<ReviewResult>?>()
    var errorMessageMovieReview = MutableLiveData<String>()

    fun getReview(id: Int) {
        val call = apiService.getMovieReviews(id)
        call.enqueue(object : Callback<Review?> {
            override fun onResponse(call: Call<Review?>, response: retrofit2.Response<Review?>) {
                if (response.isSuccessful) {
                    reviewList.value = response.body()?.results
                }
            }

            override fun onFailure(call: Call<Review?>, t: Throwable) {
                reviewList.postValue(null)

                errorMessageMovieReview.postValue(
                    R.string.error_message_movie_review.toString(),
                )
            }
        })
    }
}
