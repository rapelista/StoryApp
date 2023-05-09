package com.gvstang.dicoding.latihan.storyapp.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.gvstang.dicoding.latihan.storyapp.api.ApiConfig
import com.gvstang.dicoding.latihan.storyapp.api.data.Login
import com.gvstang.dicoding.latihan.storyapp.api.response.LoginResponse
import com.gvstang.dicoding.latihan.storyapp.model.UserModel
import com.gvstang.dicoding.latihan.storyapp.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference): ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private var _responseBody = MutableLiveData<LoginResponse>()
    val responseBody: LiveData<LoginResponse> = _responseBody

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun login(user: UserModel) {
        viewModelScope.launch {
            pref.login(user)
        }
    }

    fun loginApi(user: Login) {
        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().login(user)
        client.enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful) {
                    _responseBody.value = response.body()
                } else {
                    _isError.value = true
                }

                _isLoading.value = false

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("loginApi", t.toString())
            }

        })
    }
}
