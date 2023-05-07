package com.gvstang.dicoding.latihan.storyapp.view.register

import android.animation.AnimatorSet
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gvstang.dicoding.latihan.storyapp.R
import com.gvstang.dicoding.latihan.storyapp.databinding.ActivityRegisterBinding
import com.gvstang.dicoding.latihan.storyapp.model.UserModel
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
                val name = edtName.text.toString().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()

                if(name.isNotEmpty() && email.isNotEmpty() && password.length >= 8) {
                    registerViewModel.saveUser(UserModel(name, email, password, false))
                    showDialog(name)

                    edtName.text?.clear()
                    edtEmail.text?.clear()
                    edtPassword.text?.clear()
                }
            }
            btnLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }
    }

    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[RegisterViewModel::class.java]
    }

    private fun playAnimation() {
        binding.apply {
            val image = Animation().create(Animation.TRANSLATE_X, ivLogin, 3000)
            val welcome = Animation().create(Animation.ALPHA, tvRegister, 1000)
            val name = Animation().create(Animation.ALPHA, inputName)
            val email = Animation().create(Animation.ALPHA, inputEmail)
            val password = Animation().create(Animation.ALPHA, inputPassword)
            val login = Animation().create(Animation.ALPHA, btnLogin)
            val or = Animation().create(Animation.ALPHA, tvOr)
            val register = Animation().create(Animation.ALPHA, btnRegister)

            AnimatorSet().apply{
                playTogether(image, AnimatorSet().apply {
                    playSequentially(welcome, name, email, password, AnimatorSet().apply {
                        playTogether(login, or, register)
                    })
                })
            }.start()
        }
    }

    private fun showDialog(name: String) {
        MaterialAlertDialogBuilder(this@RegisterActivity, com.google.android.material.R.style.MaterialAlertDialog_Material3)
            .setTitle(resources.getString(R.string.register_success_title, name))
            .setMessage(resources.getString(R.string.register_success_message))
            .setPositiveButton(resources.getString(R.string.register_success_ok)) { _, _ ->
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }.show()
    }
}