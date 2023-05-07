package com.gvstang.dicoding.latihan.storyapp.util.animation

import android.animation.ObjectAnimator
import android.view.View

class Animation {

    companion object {
        const val ALPHA = "alpha"
        const val TRANSLATE_X = "translate_x"
    }

    fun create(mode: String, view: View, dur: Long = 500): ObjectAnimator? {
        return when(mode) {
            ALPHA -> {
                ObjectAnimator.ofFloat(view, View.ALPHA, 1f).setDuration(dur)
            }
            TRANSLATE_X -> {
                ObjectAnimator.ofFloat(view, View.TRANSLATION_X, -30f, 30f).apply {
                    duration = dur
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.REVERSE
                }
            }
            else -> null
        }
    }

}