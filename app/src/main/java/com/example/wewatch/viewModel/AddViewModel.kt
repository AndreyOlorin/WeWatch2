package com.example.wewatch.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.wewatch.model.Movie
import com.example.wewatch.model.MovieDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddViewModel(application: Application) : AndroidViewModel(application) {

    private val movieDao = MovieDB.getDb(application).getDao()

    val title = MutableLiveData<String>()
    val releaseDate = MutableLiveData<String>()
    val moviePosterPath = MutableLiveData<String>()

    val allMovies = MutableLiveData<List<Movie>>()

    fun addMovie() {
        val title = title.value
        val releaseDate = releaseDate.value
        val posterPath = moviePosterPath.value

        if (!title.isNullOrEmpty() && !releaseDate.isNullOrEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val movie = Movie(
                    null,
                    title,
                    releaseDate,
                    posterPath.orEmpty()
                )
                movieDao.insert(movie)
                loadMovies()
            }
        }
    }

    private fun loadMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.getAll().collect { movies ->
                allMovies.postValue(movies)
            }
        }
    }
}