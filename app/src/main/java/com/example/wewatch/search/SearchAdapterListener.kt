package com.example.wewatch.search

import android.view.View

interface SearchAdapterListener {
    fun onItemClick(view: View, position: Int)
}