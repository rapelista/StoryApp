package com.gvstang.dicoding.latihan.storyapp.view.login

import android.animation.AnimatorSet
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gvstang.dicoding.latihan.storyapp.R
import com.gvstang.dicoding.latihan.storyapp.databinding.ActivityLoginBinding
import com.gvstang.dicoding.latihan.storyapp.model.UserModel
import com.gvstang.dicoding.latihan.storyapp.model.UserPreference
import com.gvstang.dicoding.latihan.storyapp.util.animation.Animation
import com.gvstang.dicoding.latihan.storyapp.view.ViewModelFactory
import com.gvstang.dicoding.latihan.storyapp.view.main.MainActivity
import com.gvstang.dicoding.latihan.storyapp.view.register.RegisterActivity

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
                    if (email == user.email && password == user.password) {
                        loginViewModel.login()
                        showDialog(true)
                    } else {
                        showDialog(false)
                    }
                    edtEmail.text?.clear()
                    edtPassword.text?.clear()
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

    fun showDialog(success: Boolean) {
        MaterialAlertDialogBuilder(this@LoginActivity, com.google.android.material.R.style.MaterialAlertDialog_Material3).apply {
            if(success) {
                setMessage(resources.getString(R.string.login_success))
                setPositiveButton(resources.getString(R.string.login_success_ok)) { _, _ ->
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            } else {
                setMessage(resources.getString(R.string.login_failed))
                setPositiveButton(resources.getString(R.string.login_failed_ok)) { _, _ -> }
            }
            show()
        }
    }
}