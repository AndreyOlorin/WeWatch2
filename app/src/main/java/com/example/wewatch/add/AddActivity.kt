package com.example.wewatch.add

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wewatch.R
import com.example.wewatch.model.MovieDB
import com.squareup.picasso.Picasso

class AddActivity : AppCompatActivity(), AddView {

    private lateinit var presenter: AddPresenter
    private lateinit var titleEditText: EditText
    private lateinit var releaseDateEditText: EditText
    private lateinit var movieImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val dataBase = MovieDB.getDb(this)
        presenter = AddPresenter(this, dataBase)

        titleEditText = findViewById(R.id.movie_title)
        val searchBtn = findViewById<ImageButton>(R.id.search_btn)
        val addBtn = findViewById<Button>(R.id.add_movie)
        releaseDateEditText = findViewById(R.id.movie_release_date)
        movieImageView = findViewById(R.id.movie_imageview)

        searchBtn.setOnClickListener {
            if (titleEditText.text.isEmpty()) {
                Toast.makeText(this, "Название фильма не может быть пустым!", Toast.LENGTH_LONG).show()
            } else {
                val title = titleEditText.text.toString()
                presenter.searchMovie(title)
            }
        }

        addBtn.setOnClickListener {
            val title = titleEditText.text.toString()
            val releaseDate = releaseDateEditText.text.toString()
            val posterPath = (movieImageView.tag as? String)?.takeIf { it.isNotEmpty() }
            presenter.addMovie(title, releaseDate, posterPath ?: "")
        }
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showMovieAdded() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun setMovieDetails(title: String, releaseDate: String, posterPath: String) {
        titleEditText.setText(title)
        releaseDateEditText.setText(releaseDate)
        movieImageView.tag = posterPath

        if (posterPath.isEmpty()) {
            movieImageView.setImageResource(R.drawable.no_image)
        } else {
            Picasso.get().load(posterPath).into(movieImageView)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            presenter.handleSearchResult(data)
        }
    }

    companion object {
        const val SEARCH_ACTIVITY_REQUEST_CODE = 2
    }
}
