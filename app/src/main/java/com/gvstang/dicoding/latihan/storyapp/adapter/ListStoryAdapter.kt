package com.gvstang.dicoding.latihan.storyapp.adapter

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gvstang.dicoding.latihan.storyapp.R
import com.gvstang.dicoding.latihan.storyapp.api.data.Story
import com.gvstang.dicoding.latihan.storyapp.databinding.ListStoryBinding

class ListStoryAdapter(private val listStory: ArrayList<Story>):
    RecyclerView.Adapter<ListStoryAdapter.ViewHolder>() {

    private lateinit var binding: ListStoryBinding

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListStoryAdapter.ViewHolder {
        binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ListStoryAdapter.ViewHolder, position: Int) {
        val (name, photoUrl) = listStory[position]
        binding.tvName.text = holder.itemView.context.getString(R.string.posted_by, name)
        Glide.with(holder.itemView)
            .load(photoUrl)
            .into(binding.ivPhoto)

        if (position == listStory.size - 1) {
            binding.listLayout.apply {
                setPadding(
                    paddingLeft,
                    paddingTop,
                    paddingRight,
                    16
                )
            }
        }

        if (position == 0) {
            binding.listLayout.apply {
                setPadding(
                    paddingLeft,
                    16,
                    paddingRight,
                    paddingBottom
                )
            }
        }
    }

    override fun getItemCount(): Int = listStory.size
}