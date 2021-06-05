package id.test.infomovie.ui.genres

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import id.test.infomovie.BuildConfig
import id.test.infomovie.data.retrofit.ApiResponse
import id.test.infomovie.data.retrofit.ApiService
import kotlinx.coroutines.Dispatchers

class ListGenresViewModel() : ViewModel() {
    fun getGenresList() = liveData(Dispatchers.IO){
        val hashMap = HashMap<String, String>()

        hashMap["api_key"] = BuildConfig.API_KEY;

        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = ApiService.RetrofitBuilder.apiService.getGenres(hashMap)))
        } catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}