package com.gvstang.dicoding.latihan.storyapp.util.animation

import android.animation.ObjectAnimator
import android.view.View

class Animation {

    companion object {
        const val ALPHA = "alpha"
        const val ALPHA_OFF = "alpha_off"
        const val TRANSLATE_X = "translate_x"
        const val FAB_CAMERA_ON = "fab_camera_on"
        const val FAB_CAMERA_OFF = "fab_camera_off"
        const val FAB_GALLERY_ON = "fab_gallery_on"
        const val FAB_GALLERY_OFF = "fab_gallery_off"
    }

    fun create(mode: String, view: View, dur: Long = 500): ObjectAnimator? {
        return when(mode) {
            ALPHA -> {
                ObjectAnimator.ofFloat(view, View.ALPHA, 1f).setDuration(dur)
            }
            ALPHA_OFF -> {
                ObjectAnimator.ofFloat(view, View.ALPHA, 0f).setDuration(dur)
            }
            TRANSLATE_X -> {
                ObjectAnimator.ofFloat(view, View.TRANSLATION_X, -30f, 30f).apply {
                    duration = dur
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.REVERSE
                }
            }
            FAB_CAMERA_ON -> {
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, -165f).setDuration(dur)
            }
            FAB_GALLERY_ON -> {
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, -330f).setDuration(dur)
            }
            FAB_CAMERA_OFF, FAB_GALLERY_OFF -> {
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0f).setDuration(dur)
            }
            else -> null
        }
    }

}