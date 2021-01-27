package com.example.ds3companion

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.ds3companion.fragment.AccountsFragment
import com.example.ds3companion.fragment.BossesFragment
import com.example.ds3companion.fragment.ChatFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.type.LatLng

lateinit var fusedLocationProviderClient: FusedLocationProviderClient

class MainActivity : AppCompatActivity() {

    var locationGPS: String = " ";

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.itemIconTintList = null
        bottomNavigationView.itemIconSize = 128

        bottomNavigationView.setOnNavigationItemSelectedListener{menuItem: MenuItem ->
            when(menuItem.itemId){
                R.id.chat -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainer, BossesFragment()
                    )
                    transaction.commit()
                }
                R.id.chat2 -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainer, ChatFragment(this, locationGPS)
                    )
                    transaction.commit()
                }
                R.id.chat3 -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainer, AccountsFragment())
                    transaction.commit()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
        bottomNavigationView.selectedItemId = R.id.chat

        val adView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        MobileAds.initialize(this)
        initGPSValues()
        getUserLocation()
    }

    private fun initGPSValues(){
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        if(isTaskRoot){
            builder.setMessage(getString(R.string.message_exitApp))
            builder.setPositiveButton(getString(R.string.message_exitYES)) { _, _ ->
                this.finish()
            }
            builder.setNegativeButton(getString(R.string.message_exitNO)) { dialog, _ ->
                dialog.cancel()
            }
            val alertDialog = builder.create()
            alertDialog.show()
        } else{
            super.onBackPressed()
        }
    }

    private fun getUserLocation() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                locationGPS = it.latitude.toString() + getString(R.string.message_gpsDelimeter) + it.longitude.toString();
                println(it.latitude.toString())
                println(it.longitude.toString())
            }
            return
        } else {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                locationGPS = it.latitude.toString() + getString(R.string.message_gpsDelimeter) + it.longitude.toString();
                println(it.latitude.toString())
                println(it.longitude.toString())
            }
        }
    }

}