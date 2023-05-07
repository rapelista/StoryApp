package com.gvstang.dicoding.latihan.storyapp.util.view

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputLayout
import com.gvstang.dicoding.latihan.storyapp.R

class MyPasswordEditText(context: Context, attrs: AttributeSet?) :
    TextInputLayout(context, attrs) {

    init {
        endIconMode = END_ICON_PASSWORD_TOGGLE
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
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