package com.gvstang.dicoding.latihan.storyapp.view.add_story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gvstang.dicoding.latihan.storyapp.api.ApiConfig
import com.gvstang.dicoding.latihan.storyapp.api.data.Story
import com.gvstang.dicoding.latihan.storyapp.api.response.NewStoryResponse
import com.gvstang.dicoding.latihan.storyapp.util.MyFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

class AddStoryViewModel: ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun addStory(story: Story, token: String) {
        _isLoading.value = true
        _isSuccess.value = false

        val file = MyFile(null).reduceFile(File(story.photoPath))
        val description = story.description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        val client = ApiConfig.getApiService().stories(
            "Bearer $token",
            imageMultiPart,
            description
        )
        client.enqueue(object : retrofit2.Callback<NewStoryResponse> {
            override fun onResponse(
                call: Call<NewStoryResponse>,
                response: Response<NewStoryResponse>,
            ) {
                if(response.isSuccessful) {
                    if(response.body()?.error == false) {
                        _isSuccess.value = true
                    }
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<NewStoryResponse>, t: Throwable) {
                Log.d("onFailure:response.body", t.toString())
            }
        })

    }

}