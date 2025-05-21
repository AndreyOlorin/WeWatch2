package com.example.wewatch.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.wewatch.R
import com.example.wewatch.viewModel.AddViewModel
import com.squareup.picasso.Picasso

class AddActivity : AppCompatActivity() {

    private val addViewModel: AddViewModel by viewModels()

    private lateinit var titleEditText: EditText
    private lateinit var releaseDateEditText: EditText
    private lateinit var movieImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        titleEditText = findViewById(R.id.movie_title)
        val searchBtn = findViewById<ImageButton>(R.id.search_btn)
        val addBtn = findViewById<Button>(R.id.add_movie)
        releaseDateEditText = findViewById(R.id.movie_release_date)
        movieImageView = findViewById(R.id.movie_imageview)

        addViewModel.title.observe(this, Observer {
            titleEditText.setText(it)
        })

        addViewModel.releaseDate.observe(this, Observer {
            releaseDateEditText.setText(it)
        })

        addViewModel.moviePosterPath.observe(this, Observer {
            movieImageView.tag = it
            if (it.isNotEmpty()) {
                Picasso.get().load(it).into(movieImageView)
            }
        })

        addViewModel.allMovies.observe(this, Observer { movies ->
            Toast.makeText(this, "Список фильмов обновлен", Toast.LENGTH_SHORT).show()
        })

        searchBtn.setOnClickListener {
            if (titleEditText.text.isEmpty()) {
                Toast.makeText(
                    this,
                    "Название фильма не может быть пустым",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val title = titleEditText.text.toString()
                val intent = Intent(this@AddActivity, SearchActivity::class.java)
                intent.putExtra(SearchActivity.SEARCH_QUERY, title)
                startActivityForResult(intent, SEARCH_ACTIVITY_REQUEST_CODE)
            }
        }

        addBtn.setOnClickListener {
            val title = titleEditText.text.toString().trim()
            val releaseDate = releaseDateEditText.text.toString().trim()
            val posterPath = movieImageView.tag?.toString().orEmpty()

            if (title.isEmpty() || releaseDate.isEmpty()) {
                Toast.makeText(
                    this,
                    "Поля не могут быть пустыми",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                addViewModel.title.value = title
                addViewModel.releaseDate.value = releaseDate
                addViewModel.moviePosterPath.value = posterPath

                addViewModel.addMovie()
                Toast.makeText(this, "Фильм успешно добавлен", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        this@AddActivity.runOnUiThread {
            addViewModel.title.value = data?.getStringExtra(SearchActivity.EXTRA_TITLE)
            addViewModel.releaseDate.value = data?.getStringExtra(SearchActivity.EXTRA_RELEASE_DATE)
            addViewModel.moviePosterPath.value = data?.getStringExtra(SearchActivity.EXTRA_POSTER_PATH)
        }
    }

    companion object {
        const val SEARCH_ACTIVITY_REQUEST_CODE = 2
    }
}