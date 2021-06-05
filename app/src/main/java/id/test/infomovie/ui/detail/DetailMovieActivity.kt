package id.test.infomovie.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import id.test.infomovie.BuildConfig
import id.test.infomovie.adapter.ReviewListAdapter
import id.test.infomovie.data.model.review.ResultsDetail
import id.test.infomovie.data.model.review.Review
import id.test.infomovie.data.retrofit.ApiResponse
import id.test.infomovie.databinding.ActivityDetailMovieBinding

class DetailMovieActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var list: MutableList<ResultsDetail>
    private lateinit var review: Review

    //    private lateinit var review: Review
    private lateinit var adapter: ReviewListAdapter
    private lateinit var binding: ActivityDetailMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val idMovies: String
        val dataMovie: id.test.infomovie.data.model.Results

        idMovies = intent.getStringExtra(EXTRA_ID)
        dataMovie = intent.getParcelableExtra(EXTRA_DATA)

        val detailMovieViewModel = ViewModelProvider(this).get(DetailMovieViewModel::class.java)

        detailMovieViewModel.getReview(idMovies).observe(this, Observer {
            it?.let { apiResponse ->
               when(apiResponse.apiStatus){
                   ApiResponse.ApiStatus.SUCCESS -> {

                       binding.rvList.visibility = View.VISIBLE

//                       list.clear()

                       review = apiResponse.data?.body()!!

                       list.addAll(apiResponse.data?.body()!!.results)

                       if (!list.isEmpty() && list != null){
                           binding.rvList.visibility = View.VISIBLE
                           binding.llKosong.visibility = View.GONE
                           adapter.apply {
                               notifyDataSetChanged()
                           }
                       }else{
                           binding.rvList.visibility = View.GONE
                           binding.llKosong.visibility = View.VISIBLE
                       }
                   }
                   ApiResponse.ApiStatus.ERROR -> {
                       binding.rvList.visibility = View.VISIBLE
                       Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                   }
                   ApiResponse.ApiStatus.LOADING -> {
                       binding.rvList.visibility = View.GONE

                   }
               }
            }
        })

        Glide.with(this).load(BuildConfig.SMALL_IMAGE_URL+""+dataMovie.poster_path).into(binding.imageView)
        binding.name.text = dataMovie.title
        binding.vote.text = dataMovie.vote_average.toString()
        binding.txtOverview.text = dataMovie.overview

        binding.rvList.layoutManager = GridLayoutManager(this@DetailMovieActivity, 1)
        list = mutableListOf<id.test.infomovie.data.model.review.ResultsDetail>()
        adapter = ReviewListAdapter(this, list)
        binding.rvList.adapter = adapter
    }
}