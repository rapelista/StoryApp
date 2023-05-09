package com.gvstang.dicoding.latihan.storyapp.view.detail

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gvstang.dicoding.latihan.storyapp.R
import com.gvstang.dicoding.latihan.storyapp.databinding.FragmentDetailBinding

class DetailFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = arguments
        if(bundle != null) {
            val photoUrl = bundle.getString(PHOTO_URL)
            val name = bundle.getString(NAME)
            val description = bundle.getString(DESCRIPTION)
            val createdAt = bundle.getString(CREATED_AT)

            binding.apply {
                Glide.with(view.context)
                    .load(photoUrl)
                    .into(ivDetailPhoto)
                tvDetailName.text = name
                tvDetailDesc.text = description
                tvDetailDate.text = resources.getString(R.string.posted_date, createdAt)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("onDestroyView", "anu")
        _binding = null
    }

    companion object {
        const val TAG = "ModalBottomSheet"
        const val NAME = "name"
        const val PHOTO_URL = "photoUrl"
        const val DESCRIPTION = "description"
        const val CREATED_AT = "createdAt"
    }

}