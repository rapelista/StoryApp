package com.gvstang.dicoding.latihan.storyapp.view.main

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gvstang.dicoding.latihan.storyapp.R
import com.gvstang.dicoding.latihan.storyapp.adapter.ListStoryAdapter
import com.gvstang.dicoding.latihan.storyapp.adapter.StoryPagingAdapter
import com.gvstang.dicoding.latihan.storyapp.api.response.Story
import com.gvstang.dicoding.latihan.storyapp.databinding.ActivityMainBinding
import com.gvstang.dicoding.latihan.storyapp.model.UserModel
import com.gvstang.dicoding.latihan.storyapp.model.UserPreference
import com.gvstang.dicoding.latihan.storyapp.util.MyFile
import com.gvstang.dicoding.latihan.storyapp.util.animation.Animation
import com.gvstang.dicoding.latihan.storyapp.view.ViewModelFactory
import com.gvstang.dicoding.latihan.storyapp.view.add_story.AddStoryActivity
import com.gvstang.dicoding.latihan.storyapp.view.detail.DetailFragment
import com.gvstang.dicoding.latihan.storyapp.view.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private var fabClicked = false

    private var user: UserModel? = null
    private val listStory = ArrayList<Story>()

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var currentPhotoPath: String

    var modalDetail = DetailFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
    }

    private fun setupView() {
        binding.apply {
            setupVisibility()

            tvMain.text = resources.getString(R.string.tv_main, user?.name)

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

            fabCamera.setOnClickListener {
                startCamera()
            }

            fabGallery.setOnClickListener {
                startGallery()
            }
        }
    }

    private fun setupVisibility() {
        binding.apply {
            tvMain.isVisible = true
            btnLogout.isVisible = true
            rvStory.isVisible = true
            fabAddStory.isVisible = true
            fabCamera.isVisible = true
            fabGallery.isVisible = true
            ivMain.isVisible = false
        }
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        MyFile(application).create().also {

            val photoUri: Uri = FileProvider.getUriForFile(
                this@MainActivity,
                "com.gvstang.dicoding.latihan.storyapp",
                it
            )

            currentPhotoPath = it.absolutePath

            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

            launcherCamera.launch(intent)
        }
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)

            intent.putExtra(AddStoryActivity.PHOTO_PATH, currentPhotoPath)
            intent.putExtra(DetailFragment.NAME, user?.name)
            intent.putExtra(AddStoryActivity.TOKEN, user?.token)

            startActivity(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherGallery.launch(chooser)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg = it.data?.data as Uri

            MyFile(application).fromUri(selectedImg).also { file ->
                val intent = Intent(this@MainActivity, AddStoryActivity::class.java)

                intent.putExtra(AddStoryActivity.PHOTO_PATH, file.absolutePath)
                intent.putExtra(DetailFragment.NAME, user?.name)
                intent.putExtra(AddStoryActivity.TOKEN, user?.token)

                startActivity(intent)
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
                playSplashAnimation()
            } else {
                this.user = user

                setupView()
                setupRecyclerView()

                getStories(user.token)
            }
        }
    }

    private fun getStories(token: String) {
        val adapter = StoryPagingAdapter()
        binding.rvStory.adapter = adapter
        mainViewModel.stories(token).observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun playSplashAnimation() {
        val ivAlpha = Animation(resources).create(Animation.ALPHA, binding.ivMain, 3000)

        AnimatorSet().apply {
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }
            })
            play(ivAlpha)
        }.start()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.apply {
            rvStory.layoutManager = layoutManager
            rvStory.adapter = ListStoryAdapter(listStory)
        }
    }

    private fun playAnimation(state: Boolean) {
        val fabCamera: ObjectAnimator?
        val fabGallery: ObjectAnimator?
        val fabCameraAlpha: ObjectAnimator?
        val fabGalleryAlpha: ObjectAnimator?

        if(state) {
            fabCamera = Animation(resources).create(Animation.FAB_CAMERA_ON, binding.fabCamera)
            fabCameraAlpha = Animation(resources).create(Animation.ALPHA, binding.fabCamera)
            fabGallery = Animation(resources).create(Animation.FAB_GALLERY_ON, binding.fabGallery)
            fabGalleryAlpha = Animation(resources).create(Animation.ALPHA, binding.fabGallery)
        } else {
            fabCamera = Animation(resources).create(Animation.FAB_CAMERA_OFF, binding.fabCamera)
            fabCameraAlpha = Animation(resources).create(Animation.ALPHA_OFF, binding.fabCamera)
            fabGallery = Animation(resources).create(Animation.FAB_GALLERY_OFF, binding.fabGallery)
            fabGalleryAlpha = Animation(resources).create(Animation.ALPHA_OFF, binding.fabGallery)
        }

        AnimatorSet().apply {
            playTogether( fabCamera, fabCameraAlpha, fabGallery, fabGalleryAlpha)
        }.start()
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