package com.gvstang.dicoding.latihan.storyapp.util

import com.gvstang.dicoding.latihan.storyapp.api.response.Story

object DataDummy {

    fun generate(): List<Story> {
        val data = ArrayList<Story>()
        for(i in 0 until 10) {
            data.add(Story(
                id = "story-RRXghhvQZla2wUTU",
                name = "Dicoding",
                description = "Pendidikan formal dan ijazah saja tidak cukup untuk self growth",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1648720873748_H78HnWtO.jpg",
                createdAt = "2022-03-31T10:01:13.750Z",
                lat = -7.9786395,
                lon = 112.5617421
            ))
        }
        return data
    }

}