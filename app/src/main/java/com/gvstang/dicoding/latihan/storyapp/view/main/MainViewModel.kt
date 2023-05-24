package com.gvstang.dicoding.latihan.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gvstang.dicoding.latihan.storyapp.api.response.Story
import com.gvstang.dicoding.latihan.storyapp.data.StoryRepository
import com.gvstang.dicoding.latihan.storyapp.model.UserModel
import com.gvstang.dicoding.latihan.storyapp.model.UserPreference
import kotlinx.coroutines.launch

class MainViewModel(private val pref: UserPreference? = null, private val storyRepository: StoryRepository) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref!!.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref!!.logout()
        }
    }

    fun stories(token: String): LiveData<PagingData<Story>> {
        return storyRepository.getStories(token).cachedIn(viewModelScope)
    }

}