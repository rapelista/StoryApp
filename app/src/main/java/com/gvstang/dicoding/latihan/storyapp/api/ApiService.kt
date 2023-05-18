package com.gvstang.dicoding.latihan.storyapp.api

import com.gvstang.dicoding.latihan.storyapp.api.data.Login
import com.gvstang.dicoding.latihan.storyapp.api.data.Register
import com.gvstang.dicoding.latihan.storyapp.api.response.LoginResponse
import com.gvstang.dicoding.latihan.storyapp.api.response.NewStoryResponse
import com.gvstang.dicoding.latihan.storyapp.api.response.RegisterResponse
import com.gvstang.dicoding.latihan.storyapp.api.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @POST("register")
    fun register(
        @Body body: Register
    ): Call<RegisterResponse>

    @POST("login")
    fun login(
        @Body body: Login
    ): Call<LoginResponse>

    @GET("stories")
    fun stories(
        @Header("Authorization") auth: String,
        @Query("location") location: Int = 1
    ): Call<StoriesResponse>

    @Multipart
    @POST("stories")
    fun stories(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ) : Call<NewStoryResponse>
}