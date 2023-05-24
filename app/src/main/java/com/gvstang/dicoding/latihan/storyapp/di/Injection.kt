package com.gvstang.dicoding.latihan.storyapp.di

import com.gvstang.dicoding.latihan.storyapp.api.ApiConfig
import com.gvstang.dicoding.latihan.storyapp.data.StoryRepository

object Injection {
    fun provideRepository(): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository(apiService)
    }
}