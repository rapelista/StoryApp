package com.gvstang.dicoding.latihan.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.gvstang.dicoding.latihan.storyapp.api.ApiService
import com.gvstang.dicoding.latihan.storyapp.api.response.Story

class StoryRepository(private val apiService: ApiService) {
    fun getStories(token: String): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }
}