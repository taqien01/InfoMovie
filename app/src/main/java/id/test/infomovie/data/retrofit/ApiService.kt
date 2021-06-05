package id.test.infomovie.data.retrofit

import id.test.infomovie.data.model.ListGenres
import id.test.infomovie.data.model.MoviesGenres
import id.test.infomovie.data.model.review.Review
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

class ApiService {
    interface ApiInterface{
        @GET("3/genre/movie/list")
        suspend fun getGenres(@QueryMap hashMap: HashMap<String, String> = HashMap()): Response<ListGenres>

        @GET("3/discover/movie")
        suspend fun getMovie(@QueryMap hashMap: HashMap<String, String> = HashMap()): Response<MoviesGenres>

        @GET("3/movie/{movie_id}/reviews")
        suspend fun getReview(@Path("movie_id") movieId: String, @QueryMap hashMap: HashMap<String, String> = HashMap()): Response<Review>
    }

    object RetrofitBuilder {

        private const val BASE_URL = "https://api.themoviedb.org/"

        private fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build() //Doesn't require the adapter
        }

        val apiService: ApiInterface = getRetrofit().create(ApiInterface::class.java)
    }
}