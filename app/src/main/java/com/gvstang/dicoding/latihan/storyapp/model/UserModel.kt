package com.gvstang.dicoding.latihan.storyapp.model

data class UserModel(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val token: String,
    val isLogin: Boolean
)