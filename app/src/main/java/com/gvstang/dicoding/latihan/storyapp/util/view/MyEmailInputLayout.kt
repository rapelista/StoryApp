package com.gvstang.dicoding.latihan.storyapp.util.view

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout
import com.gvstang.dicoding.latihan.storyapp.R

class MyEmailInputLayout(context: Context, attrs: AttributeSet?) :
    TextInputLayout(context, attrs) {

    private var _isValidated = MutableLiveData<Boolean>()
    val isValidated: LiveData<Boolean> = _isValidated


    override fun onFinishInflate() {
        super.onFinishInflate()
        editText?.setOnFocusChangeListener { _, hasFocus ->
            this.apply {
                isErrorEnabled = false
                if(!hasFocus) {
                    if(editText!!.text.isEmpty()) {
                        error = resources.getString(R.string.email_empty)
                    } else if(!isValidEmail(editText!!.text)) {
                        error = resources.getString(R.string.email_error)
                    } else {
                        _isValidated.value = true
                    }
                }
            }
        }
    }

    private fun isValidEmail(input: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(input).matches()
    }
}