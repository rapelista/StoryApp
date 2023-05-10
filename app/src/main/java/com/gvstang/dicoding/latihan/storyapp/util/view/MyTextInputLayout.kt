package com.gvstang.dicoding.latihan.storyapp.util.view

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout
import com.gvstang.dicoding.latihan.storyapp.R

open class MyTextInputLayout(context: Context, attrs: AttributeSet?) : TextInputLayout   (context, attrs) {

    private var _isValidated = MutableLiveData<Boolean>()
    val isValidated: LiveData<Boolean> = _isValidated

    override fun onFinishInflate() {
        super.onFinishInflate()
        editText?.setOnFocusChangeListener { _, hasFocus ->
            this.apply {
                _isValidated.value = false
                isErrorEnabled = false
                if(!hasFocus) {
                    if(editText!!.text.isEmpty()) {
                        val field = hint.toString().lowercase()
                        error = resources.getString(R.string.empty_field, field)
                    } else {
                        _isValidated.value = true
                    }
                }
            }
        }
    }

}