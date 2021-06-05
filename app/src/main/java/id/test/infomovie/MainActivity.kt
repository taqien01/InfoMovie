package id.test.infomovie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import id.test.infomovie.adapter.GenreListAdapter
import id.test.infomovie.data.model.Genres

import androidx.recyclerview.widget.LinearLayoutManager
import id.test.infomovie.data.model.ListGenres
import id.test.infomovie.data.retrofit.ApiResponse
import id.test.infomovie.databinding.ActivityMainBinding
import id.test.infomovie.ui.genres.ListGenresViewModel
import id.test.infomovie.ui.movie.MoviesActivity

class MainActivity : AppCompatActivity() {

    private lateinit var list: MutableList<Genres>
    private lateinit var genres: ListGenres
    private lateinit var listGenreAdapter: GenreListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.rvList.layoutManager = GridLayoutManager(this@MainActivity, 2);
        list = mutableListOf<Genres>()
        listGenreAdapter = GenreListAdapter(this, list)
        binding.rvList.adapter = listGenreAdapter

        val listGenresViewModel = ViewModelProvider(this).get(ListGenresViewModel::class.java)

        listGenresViewModel.getGenresList().observe(this, Observer {
            it?.let { apiResponse ->
                when (apiResponse.apiStatus){
                    ApiResponse.ApiStatus.SUCCESS -> {

                        binding.rvList.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE

                        list.clear()

                        genres = apiResponse.data?.body()!!

                        list.addAll(genres.genres)

                        listGenreAdapter.apply {
                            notifyDataSetChanged()
                        }

                    }
                    ApiResponse.ApiStatus.ERROR -> {
                        binding.rvList.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    ApiResponse.ApiStatus.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.rvList.visibility = View.GONE
                    }
                }
            }
        })

        setListClickAction()
    }

    private fun setListClickAction() {
        listGenreAdapter.setOnItemClickCallback(object : GenreListAdapter.OnItemClickCallback{
            override fun onItemClick(data: Genres) {

                val manageDetailIntent = Intent(this@MainActivity,
                    MoviesActivity::class.java).apply {
                    putExtra(MoviesActivity.EXTRA_NAME,
                        data.name)
                    putExtra(MoviesActivity.EXTRA_ID,
                        data.id.toString())
                }
                startActivity(manageDetailIntent)
            }
        })
    }
}