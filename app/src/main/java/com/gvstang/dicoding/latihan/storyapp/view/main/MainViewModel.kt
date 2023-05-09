package com.gvstang.dicoding.latihan.storyapp.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.gvstang.dicoding.latihan.storyapp.api.ApiConfig
import com.gvstang.dicoding.latihan.storyapp.api.response.StoriesResponse
import com.gvstang.dicoding.latihan.storyapp.api.response.Story
import com.gvstang.dicoding.latihan.storyapp.model.UserModel
import com.gvstang.dicoding.latihan.storyapp.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val pref: UserPreference) : ViewModel() {

    private var _listStory = MutableLiveData<ArrayList<Story>>()
    val listStory: LiveData<ArrayList<Story>> = _listStory

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun getListStory(token: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().stories("Bearer $token")
        client.enqueue(object : retrofit2.Callback<StoriesResponse> {
            override fun onResponse(
                call: retrofit2.Call<StoriesResponse>,
                response: Response<StoriesResponse>,
            ) {
                if (response.isSuccessful) {
                    val data = ArrayList<Story>()
                    val responseBody = response.body()

                    responseBody?.listStory?.map {
                        data.add(it)
                    }
                    _listStory.value = data
                }

                _isLoading.value = false
            }

            override fun onFailure(call: retrofit2.Call<StoriesResponse>, t: Throwable) {
                Log.e("getListStory", t.toString())
            }
        })
    }

}