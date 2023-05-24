package com.gvstang.dicoding.latihan.storyapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gvstang.dicoding.latihan.storyapp.api.ApiService
import com.gvstang.dicoding.latihan.storyapp.api.response.Story

class StoryPagingSource(private val apiService: ApiService, private val token: String) : PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.stories(
                "Bearer $token",
                page
            ).listStory

            LoadResult.Page(
                data = responseData,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}