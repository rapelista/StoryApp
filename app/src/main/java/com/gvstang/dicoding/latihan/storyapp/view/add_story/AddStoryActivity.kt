package com.gvstang.dicoding.latihan.storyapp.view.add_story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentPhotoPath = intent.getStringExtra(PHOTO_PATH).toString()
        name = intent.getStringExtra(NAME).toString()
        token = intent.getStringExtra(TOKEN).toString()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupLocation()
        setupView()
        setupViewModel()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    setupLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    setupLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupLocation() {
        if     (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                this@AddStoryActivity.location = location

                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude
                    Log.d("location", location.toString())
                } else {
                    Toast.makeText(
                        this@AddStoryActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
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
                    bundle.putDouble(DetailFragment.LATITUDE, location!!.latitude)
                    bundle.putDouble(DetailFragment.LONGITUDE, location!!.longitude)

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