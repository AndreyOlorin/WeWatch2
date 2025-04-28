package com.example.wewatch.search

import com.example.wewatch.model.Item

interface SearchView {
    fun showSearchResults(items: List<Item>)
    fun showError(message: String)
    fun showLoading()
    fun showEmptyState()
}