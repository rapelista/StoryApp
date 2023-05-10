package com.gvstang.dicoding.latihan.storyapp.util.animation

import android.animation.ObjectAnimator
import android.content.res.Resources
import android.view.View

class Animation(resources: Resources) {

    companion object {
        const val ALPHA = "alpha"
        const val ALPHA_OFF = "alpha_off"
        const val TRANSLATE_X = "translate_x"
        const val FAB_CAMERA_ON = "fab_camera_on"
        const val FAB_CAMERA_OFF = "fab_camera_off"
        const val FAB_GALLERY_ON = "fab_gallery_on"
        const val FAB_GALLERY_OFF = "fab_gallery_off"
        const val BUTTON_SCALE_X = "button_scale_x"
        const val BUTTON_SCALE_Y = "button_scale_y"
        const val BUTTON_TRANSLATE = "button_translate"
    }

    private var density: Float = 0.0f

    init {
        density = resources.displayMetrics.density
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
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0 - view.height - density * 16).setDuration(dur)
            }
            FAB_GALLERY_ON -> {
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0 - view.height * 2 - density * 32).setDuration(dur)
            }
            FAB_CAMERA_OFF, FAB_GALLERY_OFF -> {
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0f).setDuration(dur)
            }
            BUTTON_SCALE_X -> {
                ObjectAnimator.ofFloat(view, View.SCALE_X, 0f).setDuration(1500)
            }
            BUTTON_SCALE_Y -> {
                ObjectAnimator.ofFloat(view, View.SCALE_Y, 0f).setDuration(1500)
            }
            BUTTON_TRANSLATE -> {
                ObjectAnimator.ofFloat(view, View.TRANSLATION_X, view.width / 2f).setDuration(1500)
            }
            else -> null
        }
    }

}