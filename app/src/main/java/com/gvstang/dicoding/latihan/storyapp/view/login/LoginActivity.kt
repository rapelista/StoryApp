package com.gvstang.dicoding.latihan.storyapp.view.login

import android.animation.AnimatorSet
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gvstang.dicoding.latihan.storyapp.R
import com.gvstang.dicoding.latihan.storyapp.api.data.Login
import com.gvstang.dicoding.latihan.storyapp.databinding.ActivityLoginBinding
import com.gvstang.dicoding.latihan.storyapp.model.UserModel
import com.gvstang.dicoding.latihan.storyapp.model.UserPreference
import com.gvstang.dicoding.latihan.storyapp.util.animation.Animation
import com.gvstang.dicoding.latihan.storyapp.view.ViewModelFactory
import com.gvstang.dicoding.latihan.storyapp.view.main.MainActivity
import com.gvstang.dicoding.latihan.storyapp.view.register.RegisterActivity
import com.gvstang.dicoding.latihan.storyapp.view.splash_login.SplashLoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var user: UserModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        playAnimation()
    }

    private fun setupView() {
        binding.apply {
            btnLogin.setOnClickListener {
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()

                if(email.isNotEmpty() && password.length >= 8) {
                    loginViewModel.loginApi(Login(email, password))
                }
            }

            btnRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this) { user ->
            this.user = user
            Log.d("user:Login", user.toString())
        }

        loginViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
            if(!isLoading) {
                binding.apply {
                    edtEmail.text?.clear()
                    edtPassword.text?.clear()
                }
            }
        }

        loginViewModel.responseBody.observe(this) { responseBody ->
            with(responseBody.loginResult) {
                loginViewModel.login(UserModel(
                    userId,
                    name,
                    this@LoginActivity.user.email,
                    this@LoginActivity.user.password,
                    token,
                    true
                ))
            }

            val intent = Intent(this@LoginActivity, SplashLoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        loginViewModel.isError.observe(this) { isError ->
            if(isError) {
                showDialogError()
            }
        }
    }

    private fun playAnimation() {
        binding.apply {
            val image = Animation().create(Animation.TRANSLATE_X, ivLogin, 3000)
            val welcome = Animation().create(Animation.ALPHA, tvWelcome, 1000)
            val email = Animation().create(Animation.ALPHA, inputEmail)
            val password = Animation().create(Animation.ALPHA, inputPassword)
            val login = Animation().create(Animation.ALPHA, btnLogin)
            val or = Animation().create(Animation.ALPHA, tvOr)
            val register = Animation().create(Animation.ALPHA, btnRegister)

            AnimatorSet().apply{
                playTogether(image, AnimatorSet().apply {
                    playSequentially(welcome, email, password, AnimatorSet().apply {
                        playTogether(login, or, register)
                    })
                })
            }.start()
        }
    }

    fun showDialogError() {
        MaterialAlertDialogBuilder(this@LoginActivity, com.google.android.material.R.style.MaterialAlertDialog_Material3).apply {
            setMessage(resources.getString(R.string.login_failed))
            setPositiveButton(resources.getString(R.string.login_failed_ok)) { _, _ -> }
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            pbLoading.isVisible = isLoading
            btnLogin.isVisible = !isLoading
            tvOr.isVisible = !isLoading
            btnRegister.isVisible = !isLoading
        }
    }
}