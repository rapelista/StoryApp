package com.gvstang.dicoding.latihan.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.gvstang.dicoding.latihan.storyapp.api.data.Story
import com.gvstang.dicoding.latihan.storyapp.model.UserModel
import com.gvstang.dicoding.latihan.storyapp.model.UserPreference
import kotlinx.coroutines.launch

class MainViewModel(private val pref: UserPreference) : ViewModel() {

    private var _listStory = MutableLiveData<ArrayList<Story>>()
    val listStory: LiveData<ArrayList<Story>> = _listStory

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun getListStory(token: String) {
        // loading dulu ges

        val data = ArrayList<Story>()
        data.add(Story("Jojo", "https://story-api.dicoding.dev/images/stories/photos-1683563653413_zYd37bPA.jpg"))
        data.add(Story("Akmal", "https://story-api.dicoding.dev/images/stories/photos-1683563567718_mEvLrC3u.jpg"))
        data.add(Story("Rizqi", "https://story-api.dicoding.dev/images/stories/photos-1683563566338_PUU78Mit.jpg"))


        // beres loading
        _listStory.value = data
    }

}