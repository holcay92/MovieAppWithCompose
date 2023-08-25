package com.example.movieapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.room.Dao
import com.example.movieapp.room.FavoriteMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteMovieViewModel @Inject constructor(private val dao: Dao) : ViewModel() {

    var favMovieList: LiveData<List<FavoriteMovie>> = dao.getAllItems()

    fun actionFavButton(movie: FavoriteMovie) = viewModelScope.launch {
        if (isFavorite(movie)) {
            deleteMovieFromDatabase(movie.id!!)
        } else {
            dao.insertFavorite(movie)
        }
    }

    private suspend fun deleteMovieFromDatabase(movieId: Int) {
        val movie = coroutineScope {
            dao.getMovieById(movieId)
        }
        movie?.let {
            dao.deleteFavorite(it)
        }
    }

    private fun isFavorite(movie: FavoriteMovie): Boolean {
        val favoriteMovies = favMovieList.value.orEmpty()
        return favoriteMovies.any { it.id == movie.id }
    }
}
