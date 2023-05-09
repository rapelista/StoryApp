package com.gvstang.dicoding.latihan.storyapp.util.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout
import com.gvstang.dicoding.latihan.storyapp.R

class MyPasswordEditText(context: Context, attrs: AttributeSet?) :
    TextInputLayout(context, attrs) {

    private var _isValidated = MutableLiveData<Boolean>()
    val isValidated: LiveData<Boolean> = _isValidated

    init {
        endIconMode = END_ICON_PASSWORD_TOGGLE
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                error = if(s!!.length < 8) {
                    resources.getString(R.string.error_password)
                } else {
                    null
                }
            }

            override fun afterTextChanged(s: Editable?) {
                _isValidated.value = s!!.length >= 8
            }

        })
        editText?.setOnFocusChangeListener { _, hasFocus ->
            this.apply {
                isErrorEnabled = false
                if(!hasFocus) {
                    if(editText!!.text.isEmpty()) {
                        error = resources.getString(R.string.password_empty)
                    } else if(editText!!.text.length < 8) {
                        error = resources.getString(R.string.error_password)
                    }
                }
            }
        }
    }

}