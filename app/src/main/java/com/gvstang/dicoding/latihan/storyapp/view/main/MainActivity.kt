package com.gvstang.dicoding.latihan.storyapp.view.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gvstang.dicoding.latihan.storyapp.R
import com.gvstang.dicoding.latihan.storyapp.adapter.ListStoryAdapter
import com.gvstang.dicoding.latihan.storyapp.api.response.Story
import com.gvstang.dicoding.latihan.storyapp.databinding.ActivityMainBinding
import com.gvstang.dicoding.latihan.storyapp.model.UserModel
import com.gvstang.dicoding.latihan.storyapp.model.UserPreference
import com.gvstang.dicoding.latihan.storyapp.util.animation.Animation
import com.gvstang.dicoding.latihan.storyapp.view.ViewModelFactory
import com.gvstang.dicoding.latihan.storyapp.view.detail.DetailFragment
import com.gvstang.dicoding.latihan.storyapp.view.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private var fabClicked = false

    private var user: UserModel? = null
    private val listStory = ArrayList<Story>()

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    var modalDetail = DetailFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupRecyclerView()
    }

    private fun playAnimation(state: Boolean) {
        val fabCamera: ObjectAnimator?
        val fabGallery: ObjectAnimator?
        val fabCameraAlpha: ObjectAnimator?
        val fabGalleryAlpha: ObjectAnimator?

        if(state) {
            fabCamera = Animation().create(Animation.FAB_CAMERA_ON, binding.fabCamera)
            fabCameraAlpha = Animation().create(Animation.ALPHA, binding.fabCamera)
            fabGallery = Animation().create(Animation.FAB_GALLERY_ON, binding.fabGallery)
            fabGalleryAlpha = Animation().create(Animation.ALPHA, binding.fabGallery)
        } else {
            fabCamera = Animation().create(Animation.FAB_CAMERA_OFF, binding.fabCamera)
            fabCameraAlpha = Animation().create(Animation.ALPHA_OFF, binding.fabCamera)
            fabGallery = Animation().create(Animation.FAB_GALLERY_OFF, binding.fabGallery)
            fabGalleryAlpha = Animation().create(Animation.ALPHA_OFF, binding.fabGallery)
        }

        AnimatorSet().apply {
            playTogether( fabCamera, fabCameraAlpha, fabGallery, fabGalleryAlpha)
        }.start()
    }

    private fun setupView() {
        binding.apply {
            btnLogout.setOnClickListener {
                showDialog()
            }

            fabAddStory.setOnClickListener {
                fabClicked = if(!fabClicked) {
                    playAnimation(true)
                    fabAddStory.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_arrow_up_24))
                    true
                } else {
                    fabAddStory.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_add_24))
                    playAnimation(false)
                    false
                }
            }
        }
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) { user ->
            if(!user.isLogin) {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            } else {
                this.user = user
                binding.tvMain.text = resources.getString(R.string.tv_main, user.name)
                Log.d("user:Main", user.toString())
                mainViewModel.getListStory(user.token)
            }
        }

        mainViewModel.listStory.observe(this) { listStory ->
            val adapter = ListStoryAdapter(listStory)
            binding.rvStory.adapter = adapter

            adapter.loaded.observe(this) {
                binding.lpLoading.setProgress(it * 10, true)
                if(binding.lpLoading.progress == 100) {
                    binding.lpLoading.isVisible = false
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.apply {
            rvStory.layoutManager = layoutManager
            rvStory.adapter = ListStoryAdapter(listStory)
        }
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(this@MainActivity, com.google.android.material.R.style.MaterialAlertDialog_Material3)
            .setMessage(resources.getString(R.string.logout_alert_message))
            .setNeutralButton(resources.getString(R.string.cancel)) {_, _ -> }
            .setPositiveButton(resources.getString(R.string.logout_alert_confirm)) { _, _ ->
                mainViewModel.logout()
            }.show()
    }
}