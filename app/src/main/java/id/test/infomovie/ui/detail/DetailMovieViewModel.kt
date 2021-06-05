package id.test.infomovie.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import id.test.infomovie.BuildConfig
import id.test.infomovie.data.retrofit.ApiResponse
import id.test.infomovie.data.retrofit.ApiService
import kotlinx.coroutines.Dispatchers

class DetailMovieViewModel() : ViewModel() {
    fun getReview(movieId : String) = liveData(Dispatchers.IO){
        val hashMap = HashMap<String, String>()
        val apiKey = BuildConfig.API_KEY

        hashMap["api_key"] = BuildConfig.API_KEY;

        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = ApiService.RetrofitBuilder.apiService.getReview(movieId, hashMap)))
        } catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}