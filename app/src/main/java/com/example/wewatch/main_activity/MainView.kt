package com.example.wewatch.main_activity

import com.example.wewatch.model.Movie

interface MainView {
    fun showMovies(movies: List<Movie>)
    fun showEmptyState()
    fun showToast(message: String)
}
