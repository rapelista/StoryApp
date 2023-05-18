package com.gvstang.dicoding.latihan.storyapp.view.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.gvstang.dicoding.latihan.storyapp.R
import com.gvstang.dicoding.latihan.storyapp.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private var latitude = 0.0
    private var longitude = 0.0
    private var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        latitude = intent.getDoubleExtra(LATITUDE, 0.0)
        longitude = intent.getDoubleExtra(LONGITUDE, 0.0)
        name = intent.getStringExtra(NAME).toString()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val position = LatLng(latitude, longitude)

        mMap.addMarker(MarkerOptions().position(position).title("Photo from $name"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 12.5f))
        mMap.uiSettings.isZoomControlsEnabled = true
    }

    companion object {
        const val LATITUDE = "lat"
        const val LONGITUDE = "lng"
        const val NAME = "name"
    }
}