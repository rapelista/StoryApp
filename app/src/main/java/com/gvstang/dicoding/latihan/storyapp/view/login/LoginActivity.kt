package com.gvstang.dicoding.latihan.storyapp.view.login

import android.animation.AnimatorSet
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
                inputEmail.requestFocus()
                inputPassword.requestFocus()
                inputPassword.clearFocus()

                if(inputEmail.isValidated.value == true && inputPassword.isValidated.value == true) {
                    val email = edtEmail.text.toString()
                    val password = edtPassword.text.toString()

                    loginViewModel.loginApi(Login(email, password))
                }
            }

            btnRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }

            edtPassword.setOnEditorActionListener { _, _, _ ->
                btnLogin.requestFocus()
                btnLogin.performClick()
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
        }

        loginViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
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
            val image = Animation(resources).create(Animation.TRANSLATE_X, ivLogin, 3000)
            val welcome = Animation(resources).create(Animation.ALPHA, tvWelcome, 1000)
            val email = Animation(resources).create(Animation.ALPHA, inputEmail)
            val password = Animation(resources).create(Animation.ALPHA, inputPassword)
            val login = Animation(resources).create(Animation.ALPHA, btnLogin)
            val or = Animation(resources).create(Animation.ALPHA, tvOr)
            val register = Animation(resources).create(Animation.ALPHA, btnRegister)

            AnimatorSet().apply{
                playTogether(image, AnimatorSet().apply {
                    playSequentially(welcome, email, password, AnimatorSet().apply {
                        playTogether(login, or, register)
                    })
                })
            }.start()
        }
    }

    private fun showDialogError() {
        MaterialAlertDialogBuilder(this@LoginActivity, com.google.android.material.R.style.MaterialAlertDialog_Material3).apply {
            setMessage(resources.getString(R.string.login_failed))
            setPositiveButton(resources.getString(R.string.login_failed_ok)) { _, _ ->
                binding.apply {
                    inputEmail.requestFocus()
                }
            }
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            edtEmail.isEnabled = !isLoading
            edtPassword.isEnabled = !isLoading

            pbLoading.isVisible = isLoading
            btnLogin.isVisible = !isLoading
            tvOr.isVisible = !isLoading
            btnRegister.isVisible = !isLoading
        }
    }
}