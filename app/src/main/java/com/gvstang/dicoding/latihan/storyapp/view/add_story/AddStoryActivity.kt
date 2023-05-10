package com.gvstang.dicoding.latihan.storyapp.view.add_story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gvstang.dicoding.latihan.storyapp.api.data.Story
import com.gvstang.dicoding.latihan.storyapp.databinding.ActivityAddStoryBinding
import com.gvstang.dicoding.latihan.storyapp.util.Date
import com.gvstang.dicoding.latihan.storyapp.view.detail.DetailFragment
import com.gvstang.dicoding.latihan.storyapp.view.main.MainActivity

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var addStoryViewModel: AddStoryViewModel
    private lateinit var currentPhotoPath: String
    private lateinit var name: String
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentPhotoPath = intent.getStringExtra(PHOTO_PATH).toString()
        name = intent.getStringExtra(NAME).toString()
        token = intent.getStringExtra(TOKEN).toString()

        setupView()
        setupViewModel()
    }

    private fun setupViewModel() {
        addStoryViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[AddStoryViewModel::class.java]

        addStoryViewModel.isLoading.observe(this) { isLoading ->
            if(isLoading) {
                showLoading(isLoading)
            } else {
                showLoading(false)
                if(addStoryViewModel.isSuccess.value == true) {
                    binding.btnUpload.isVisible = false
                    binding.successIndicator.isVisible = true

                    Handler(Looper.getMainLooper()).postDelayed({
                        goToMain()
                    }, 1000)
                } else {
                    showDialog()
                }
            }
        }
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Material3)
            .apply {
                setMessage(resources.getString(com.gvstang.dicoding.latihan.storyapp.R.string.upload_failed_message))
                setPositiveButton(resources.getString(com.gvstang.dicoding.latihan.storyapp.R.string.register_success_ok)) { _, _ ->
                    goToMain()
                }
            }.show()
    }

    private fun goToMain() {
        val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            btnUpload.isVisible = !isLoading
            pbLoading.isVisible = isLoading
            edtDesc.isEnabled = !isLoading
        }
    }

    private fun setupView() {
        binding.apply {
            btnPreview.setOnClickListener {
                inputDesc.requestFocus()
                inputDesc.clearFocus()

                if(inputDesc.isValidated.value == true) {
                    val date = Date(resources = resources).getNow()
                    val modalPreview = DetailFragment()
                    val bundle = Bundle()
                    val description = edtDesc.text

                    bundle.putString(DetailFragment.PHOTO_PATH, currentPhotoPath)
                    bundle.putString(DetailFragment.DESCRIPTION, description.toString())
                    bundle.putString(DetailFragment.CREATED_AT, date)
                    bundle.putString(DetailFragment.NAME, name)

                    modalPreview.arguments = bundle
                    modalPreview.show(supportFragmentManager, DetailFragment.TAG)
                }
            }

            topbar.apply {
                setNavigationOnClickListener {
                    finish()
                }
            }

            btnUpload.setOnClickListener {
                inputDesc.requestFocus()
                inputDesc.clearFocus()

                if(inputDesc.isValidated.value == true) {
                    addStoryViewModel.addStory(
                        Story(currentPhotoPath, edtDesc.text.toString()),
                        token
                    )
                }
            }
        }
    }

    companion object {
        const val TOKEN = "token"
        const val NAME = "name"
        const val PHOTO_PATH = "photoPath"
    }
}