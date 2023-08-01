package com.example.movieapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.room.Dao
import com.example.movieapp.room.FavoriteMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteMovieViewModel @Inject constructor(private val dao: Dao) : ViewModel() {

    var favMovieList: LiveData<List<FavoriteMovie>> = dao.getAllItems()

    fun actionFavButton(movie: FavoriteMovie) = viewModelScope.launch {
        val favoriteMovies = favMovieList.value

        if (favoriteMovies?.any { it.id == movie.id } == true) {
            val deleteMovie = favoriteMovies.find { it.id == movie.id }
            dao.deleteFavorite(deleteMovie!!)
        } else {
            dao.insertFavorite(movie)
        }
    }
}
