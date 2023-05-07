package com.gvstang.dicoding.latihan.storyapp.view.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gvstang.dicoding.latihan.storyapp.R
import com.gvstang.dicoding.latihan.storyapp.databinding.ActivityMainBinding
import com.gvstang.dicoding.latihan.storyapp.model.UserModel
import com.gvstang.dicoding.latihan.storyapp.model.UserPreference
import com.gvstang.dicoding.latihan.storyapp.view.ViewModelFactory
import com.gvstang.dicoding.latihan.storyapp.view.login.LoginActivity
import java.util.Locale

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private var user: UserModel? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupView()
    }

    private fun setupView() {
        binding.apply {
            btnLogout.setOnClickListener {
                showDialog()
            }
        }
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(this@MainActivity, com.google.android.material.R.style.MaterialAlertDialog_Material3)
            .setMessage(resources.getString(R.string.logout_alert_message))
            .setPositiveButton(resources.getString(R.string.logout_alert_confirm)) { _, _ ->
                mainViewModel.logout()
            }.show()
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
            }
        }
    }
}