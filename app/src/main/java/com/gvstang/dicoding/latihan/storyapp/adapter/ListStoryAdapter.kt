package com.gvstang.dicoding.latihan.storyapp.adapter

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

class ListStoryAdapter(private val listStory: ArrayList<Story>):
    RecyclerView.Adapter<ListStoryAdapter.ViewHolder>() {

    private var _loaded = MutableLiveData(0)
    val loaded: LiveData<Int> = _loaded

    private lateinit var binding: ListStoryBinding
    private lateinit var parentContext: MainActivity

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        parentContext = parent.context as MainActivity
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.apply {
            listStory[position].apply {
                Glide.with(holder.itemView)
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
                            _loaded.value = loaded.value?.plus(1)
                            return false
                        }
                    })
                    .into(ivPhoto)
                tvName.text = holder.itemView.resources.getString(R.string.posted_by, name)

                card.setOnClickListener {
                    parentContext.apply {
                        DetailFragment.apply {
                            val bundle = Bundle()

                            bundle.putString(NAME, name)
                            bundle.putString(PHOTO_URL, photoUrl)
                            bundle.putString(DESCRIPTION, description)
                            bundle.putString(CREATED_AT, Date(createdAt, holder.itemView.resources).format())
                            bundle.putDouble(LATITUDE, lat as Double)
                            bundle.putDouble(LONGITUDE, lon as Double)

                            modalDetail.arguments = bundle
                            modalDetail.show(supportFragmentManager, TAG)
                        }
                    }
                }

            }

            if (position == listStory.size - 1) {
                listLayout.apply {
                    setPadding(
                        paddingLeft,
                        paddingTop,
                        paddingRight,
                        192
                    )
                }
            }

            if (position == 0) {
                listLayout.apply {
                    setPadding(
                        paddingLeft,
                        16,
                        paddingRight,
                        paddingBottom
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int = listStory.size
}