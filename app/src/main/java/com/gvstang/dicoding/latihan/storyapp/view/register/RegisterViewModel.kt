package com.gvstang.dicoding.latihan.storyapp.view.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gvstang.dicoding.latihan.storyapp.model.UserModel
import com.gvstang.dicoding.latihan.storyapp.model.UserPreference
import kotlinx.coroutines.launch

class RegisterViewModel(private val pref: UserPreference): ViewModel() {
    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }
}