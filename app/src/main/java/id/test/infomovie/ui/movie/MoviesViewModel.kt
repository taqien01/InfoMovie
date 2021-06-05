package id.test.infomovie.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import id.test.infomovie.BuildConfig
import id.test.infomovie.data.retrofit.ApiResponse
import id.test.infomovie.data.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesViewModel() : ViewModel() {
    fun getMovies(genreId : String, page : String) = liveData(Dispatchers.IO){
        val hashMap = HashMap<String, String>()

        hashMap["api_key"] = BuildConfig.API_KEY;
        hashMap["genre_ids"] = genreId
        hashMap["page"] = page;

        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = ApiService.RetrofitBuilder.apiService.getMovie(hashMap)))
        } catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message ?: "Error Occurred!"))
        }


    }
}