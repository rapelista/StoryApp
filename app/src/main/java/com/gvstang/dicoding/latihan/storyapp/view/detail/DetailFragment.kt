package com.gvstang.dicoding.latihan.storyapp.view.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gvstang.dicoding.latihan.storyapp.R
import com.gvstang.dicoding.latihan.storyapp.databinding.FragmentDetailBinding
import com.gvstang.dicoding.latihan.storyapp.view.maps.MapsActivity
import java.io.File

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = arguments
        if(bundle != null) {
            val photoUrl = bundle.getString(PHOTO_URL)
            val name = bundle.getString(NAME)
            val description = bundle.getString(DESCRIPTION)
            val createdAt = bundle.getString(CREATED_AT)
            val photoPath = bundle.getString(PHOTO_PATH)
            val latitude = bundle.getDouble(LATITUDE)
            val longitude = bundle.getDouble(LONGITUDE)

            binding.apply {
                if(photoUrl != null) {
                    Glide.with(view.context)
                        .load(photoUrl)
                        .into(ivDetailPhoto)
                }
                if(photoPath != null) {
                    val image = File(photoPath)
                    Glide.with(view.context)
                        .load(image)
                        .into(ivDetailPhoto)
                }

                tvDetailName.text = name
                tvDetailDesc.text = description
                tvDetailDate.text = resources.getString(R.string.posted_date, createdAt)

                btnLocation.setOnClickListener {
                    Log.d("location:Detail", """
                        latitude: $latitude
                        longitude: $longitude
                    """.trimIndent())
                    val intent = Intent(activity, MapsActivity::class.java)

                    intent.putExtra(MapsActivity.LATITUDE, latitude)
                    intent.putExtra(MapsActivity.LONGITUDE, longitude)
                    intent.putExtra(MapsActivity.NAME, name)

                    startActivity(intent)
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ModalBottomSheet"
        const val NAME = "name"
        const val PHOTO_URL = "photoUrl"
        const val DESCRIPTION = "description"
        const val CREATED_AT = "createdAt"
        const val PHOTO_PATH = "photoPath"
        const val LATITUDE = "lat"
        const val LONGITUDE = "lon"
    }

}