package com.gvstang.dicoding.latihan.storyapp.api.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    val name: String,
    val photoUrl: String
): Parcelable
