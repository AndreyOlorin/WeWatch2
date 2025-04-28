package com.example.wewatch.add

interface AddView {
    fun showError(message: String)
    fun showMovieAdded()
    fun setMovieDetails(title: String, releaseDate: String, posterPath: String)
}