package com.gvstang.dicoding.latihan.storyapp.util.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.google.android.material.textfield.TextInputLayout
import com.gvstang.dicoding.latihan.storyapp.R

class MyTextInputLayout(context: Context, attrs: AttributeSet?) : TextInputLayout   (context, attrs) {

    override fun onFinishInflate() {
        super.onFinishInflate()
        super.onFinishInflate()
        editText?.setOnFocusChangeListener { _, hasFocus ->
            this.apply {
                isErrorEnabled = false
                if(!hasFocus) {
                    if(editText!!.text.isEmpty()) {
                        val field = hint.toString().lowercase()
                        error = resources.getString(R.string.empty_field, field)
                    }
                }
            }
        }
    }

}