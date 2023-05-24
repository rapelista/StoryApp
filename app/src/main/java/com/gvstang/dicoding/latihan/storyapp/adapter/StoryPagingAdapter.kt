package com.gvstang.dicoding.latihan.storyapp.adapter

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.gvstang.dicoding.latihan.storyapp.R
import com.gvstang.dicoding.latihan.storyapp.api.response.Story
import com.gvstang.dicoding.latihan.storyapp.databinding.ListStoryBinding
import com.gvstang.dicoding.latihan.storyapp.util.Date
import com.gvstang.dicoding.latihan.storyapp.view.detail.DetailFragment
import com.gvstang.dicoding.latihan.storyapp.view.main.MainActivity

class StoryPagingAdapter: PagingDataAdapter<Story, StoryPagingAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ListStoryBinding, private val parentContext: MainActivity): RecyclerView.ViewHolder(binding.root)  {
        fun bind(data: Story) {
            binding.apply {
                data.apply {
                    Glide.with(itemView)
                        .load(photoUrl)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean,
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean,
                            ): Boolean {
//                                _loaded.value = loaded.value?.plus(1)
                                return false
                            }
                        })
                        .into(ivPhoto)
                    tvName.text = itemView.resources.getString(R.string.posted_by, name)

                    card.setOnClickListener {
                        parentContext.apply {
                            DetailFragment.apply {
                                val bundle = Bundle()

                                bundle.putString(NAME, name)
                                bundle.putString(PHOTO_URL, photoUrl)
                                bundle.putString(DESCRIPTION, description)
                                bundle.putString(CREATED_AT, Date(createdAt, itemView.resources).format())
                                bundle.putDouble(LATITUDE, lat as Double)
                                bundle.putDouble(LONGITUDE, lon as Double)

                                modalDetail.arguments = bundle
                                modalDetail.show(supportFragmentManager, TAG)
                            }
                        }
                    }

                }
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if(data != null) {
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val parentContext = parent.context as MainActivity
        return MyViewHolder(binding, parentContext)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
