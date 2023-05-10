package com.gvstang.dicoding.latihan.storyapp.view.register

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
import com.gvstang.dicoding.latihan.storyapp.api.data.Register
import com.gvstang.dicoding.latihan.storyapp.databinding.ActivityRegisterBinding
import com.gvstang.dicoding.latihan.storyapp.model.UserPreference
import com.gvstang.dicoding.latihan.storyapp.util.animation.Animation
import com.gvstang.dicoding.latihan.storyapp.view.ViewModelFactory
import com.gvstang.dicoding.latihan.storyapp.view.login.LoginActivity
import java.util.Locale

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        playAnimation()
    }

    private fun setupView() {
        binding.apply {
            btnRegister.setOnClickListener {

                if(
                    inputName.isValidated.value == true && inputEmail.isValidated.value == true && inputPassword.isValidated.value == true
                ) {
                    val name = edtName.text.toString().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                    val email = edtEmail.text.toString()
                    val password = edtPassword.text.toString()

                    registerViewModel.registerApi(Register(name, email, password))
                } else {
                    inputName.requestFocus()
                    inputEmail.requestFocus()
                    inputPassword.requestFocus()
                    inputPassword.clearFocus()
                }
            }

            btnLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }

            edtPassword.setOnEditorActionListener { _, _, _ ->
                btnRegister.requestFocus()
                btnRegister.performClick()
            }
        }
    }

    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[RegisterViewModel::class.java]

        registerViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        registerViewModel.responseBody.observe(this) { responseBody ->
            if(!responseBody.error) {
                showDialog(true)
            }
        }

        registerViewModel.isError.observe(this) { isError ->
            if(isError) {
                showDialog(!isError)
            }
        }
    }

    private fun playAnimation() {
        binding.apply {
            val image = Animation(resources).create(Animation.TRANSLATE_X, ivLogin, 3000)
            val welcome = Animation(resources).create(Animation.ALPHA, tvRegister, 1000)
            val name = Animation(resources).create(Animation.ALPHA, inputName)
            val email = Animation(resources).create(Animation.ALPHA, inputEmail)
            val password = Animation(resources).create(Animation.ALPHA, inputPassword)
            val login = Animation(resources).create(Animation.ALPHA, btnLogin)
            val or = Animation(resources).create(Animation.ALPHA, tvOr)
            val register = Animation(resources).create(Animation.ALPHA, btnRegister)

            AnimatorSet().apply{
                playTogether(image, AnimatorSet().apply {
                    playSequentially(welcome, name, email, password, AnimatorSet().apply {
                        playTogether(login, or, register)
                    })
                })
            }.start()
        }
    }

    private fun showDialog(success: Boolean) {
        MaterialAlertDialogBuilder(this@RegisterActivity, com.google.android.material.R.style.MaterialAlertDialog_Material3)
            .apply {
                if(success) {
                    setTitle(resources.getString(R.string.register_success_title))
                    setMessage(resources.getString(R.string.register_success_message))
                    setPositiveButton(resources.getString(R.string.register_success_ok)) { _, _ ->
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    }
                } else {
                    setMessage(resources.getString(R.string.register_failed_message))
                    setPositiveButton(resources.getString(R.string.register_success_ok)) { _, _ -> }
                }

            }.show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            edtName.isEnabled = !isLoading
            edtEmail.isEnabled = !isLoading
            edtPassword.isEnabled = !isLoading

            pbLoading.isVisible = isLoading
            btnLogin.isVisible = !isLoading
            tvOr.isVisible = !isLoading
            btnRegister.isVisible = !isLoading
        }
    }
}