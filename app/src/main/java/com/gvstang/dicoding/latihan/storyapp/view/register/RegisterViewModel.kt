package com.gvstang.dicoding.latihan.storyapp.view.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gvstang.dicoding.latihan.storyapp.api.ApiConfig
import com.gvstang.dicoding.latihan.storyapp.api.data.Register
import com.gvstang.dicoding.latihan.storyapp.api.response.RegisterResponse
import com.gvstang.dicoding.latihan.storyapp.model.UserModel
import com.gvstang.dicoding.latihan.storyapp.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class RegisterViewModel(private val pref: UserPreference): ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private var _responseBody = MutableLiveData<RegisterResponse>()
    val responseBody: LiveData<RegisterResponse> = _responseBody

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    fun registerApi(user: Register) {
        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().register(user)
        client.enqueue(object : retrofit2.Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>,
            ) {
                if(response.isSuccessful) {
                    _responseBody.value = response.body()
                } else {
                    _isError.value = true
                }

                _isLoading.value = false
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("registerApi", t.toString())
            }

        })
    }
}