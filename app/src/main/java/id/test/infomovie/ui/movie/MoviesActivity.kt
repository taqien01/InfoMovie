package id.test.infomovie.ui.movie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.test.infomovie.R
import id.test.infomovie.adapter.MovieListAdapter
import id.test.infomovie.data.config.Constant.Constant.VIEW_TYPE_ITEM
import id.test.infomovie.data.config.Constant.Constant.VIEW_TYPE_LOADING
import id.test.infomovie.data.model.MoviesGenres
import id.test.infomovie.data.model.Results
import id.test.infomovie.data.retrofit.ApiResponse
import id.test.infomovie.databinding.ActivityMoviesBinding
import id.test.infomovie.ui.OnLoadMoreListener
import id.test.infomovie.ui.RecyclerViewLoadMoreScroll
import id.test.infomovie.ui.detail.DetailMovieActivity

class MoviesActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var list: MutableList<Results>
    private lateinit var list2: MutableList<Results>
    private lateinit var movie: MoviesGenres
    private lateinit var adapter: MovieListAdapter
    private lateinit var binding: ActivityMoviesBinding
    private lateinit var movieListViewModel: MoviesViewModel

    lateinit var idgenres: String
    var pages: Int? = null
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        setRVLayoutManager()
        setRVScrollListener()

    }

    private fun setListClickAction() {
        adapter.setOnItemClickCallback(object : MovieListAdapter.OnItemClickCallback{
            override fun onItemClick(data: Results) {
                val manageDetailIntent = Intent(this@MoviesActivity,
                    DetailMovieActivity::class.java).apply {
                    putExtra(DetailMovieActivity.EXTRA_DATA,
                        data)
                    putExtra(DetailMovieActivity.EXTRA_ID,
                        data.id.toString())
                }
                startActivity(manageDetailIntent)
            }
        })
    }

    private fun LoadMoreData() {
        //Add the Loading View
        val pages1 = pages?.plus(1)

        pages = pages1

        //adapter.addLoadingView()
        binding.progressBar.visibility = View.VISIBLE
        //Create the loadMoreItemsCells Arraylist
        list2 = ArrayList()
        //Get the number of the current Items of the main Arraylist
        val start = adapter.itemCount
        //Load 16 more items
        val end = start + 16
        //Use Handler if the items are loading too fast.
        //If you remove it, the data will load so fast that you can't even see the LoadingView
        Handler().postDelayed({
            movieListViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)

            movieListViewModel.getMovies(idgenres, pages.toString()).observe(this, Observer {
                it?.let { apiResponse ->
                    when (apiResponse.apiStatus){
                        ApiResponse.ApiStatus.SUCCESS -> {

                            binding.progressBar.visibility = View.GONE

                            movie = apiResponse.data?.body()!!

                            list2.addAll(movie.results)

                            adapter.addData(list2)
                            //Change the boolean isLoading to false
                            scrollListener.setLoaded()
                            //Update the recyclerView in the main thread
                            binding.rvList.post {
                                adapter.notifyDataSetChanged()
                            }

                        }
                        ApiResponse.ApiStatus.ERROR -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        }
                        ApiResponse.ApiStatus.LOADING -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                    }
                }
            })
        }, 3000)

    }

    private fun setRVLayoutManager() {
        mLayoutManager = GridLayoutManager(this@MoviesActivity, 2)
        binding.rvList.layoutManager = mLayoutManager
        binding.rvList.setHasFixedSize(true)

        list = mutableListOf<Results>()
        adapter = MovieListAdapter(this, list)
        binding.rvList.adapter = adapter

        movieListViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)

        idgenres = intent.getStringExtra(EXTRA_ID)
        pages = 1

        movieListViewModel.getMovies(idgenres, pages.toString()).observe(this, Observer {
            it?.let { apiResponse ->
                when (apiResponse.apiStatus){
                    ApiResponse.ApiStatus.SUCCESS -> {

                        binding.rvList.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE

                        list.clear()

                        movie = apiResponse.data?.body()!!

                        list.addAll(movie.results)

                        adapter.apply {
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

        //111`````=`~~~

        (mLayoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    VIEW_TYPE_ITEM -> 1
                    VIEW_TYPE_LOADING -> 3 //number of columns of the grid
                    else -> -1
                }
            }
        }
    }

    private fun setRVScrollListener() {
        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as GridLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                LoadMoreData()
            }
        })

        binding.rvList.addOnScrollListener(scrollListener)
    }
}