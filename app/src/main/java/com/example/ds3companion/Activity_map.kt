package com.example.ds3companion

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class Activity_map : AppCompatActivity(), OnMapReadyCallback {

    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap
    private var locationX: Double = 0.0
    private var locationY: Double = 0.0
    private var infoUser: String = " "


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)


        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMapPass: GoogleMap){
        googleMap = googleMapPass

        val intent = intent
        val extras = intent.extras
        locationX = extras!!.getDouble("locationX", 0.0)
        locationY = extras!!.getDouble("locationY", 0.0)
        infoUser = extras!!.getString("info", " ")

        val location = LatLng(locationX,locationY)
        val marker: Marker = googleMap.addMarker(MarkerOptions().position(location)?.title("User Location")?.snippet(infoUser))
        marker.showInfoWindow()
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
    }
}


