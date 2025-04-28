package com.example.wewatch.main_activity

import com.example.wewatch.model.Movie

interface MainAdapterListener {
    fun onMovieSelected(movie: Movie, isSelected: Boolean)
}