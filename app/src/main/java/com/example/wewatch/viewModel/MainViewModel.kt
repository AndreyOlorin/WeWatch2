package com.example.wewatch.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.wewatch.model.Movie
import com.example.wewatch.model.MovieDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val movieDao = MovieDB.getDb(application).getDao()

    val movies: LiveData<List<Movie>> = movieDao.getAll().asLiveData()

    private val selectedMovies = mutableSetOf<Movie>()

    fun deleteMovies(moviesToDelete: List<Movie>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (movie in moviesToDelete) {
                movieDao.delete(movie)
            }
        }
    }

    fun addMovie(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.insert(movie)
        }
    }

    fun toggleMovieSelection(movie: Movie, isSelected: Boolean) {
        if (isSelected) {
            selectedMovies.add(movie)
        } else {
            selectedMovies.remove(movie)
        }
    }

    fun getSelectedMovies(): List<Movie> {
        return selectedMovies.toList()
    }
}