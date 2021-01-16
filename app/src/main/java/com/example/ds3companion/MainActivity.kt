package com.example.ds3companion

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.ds3companion.fragment.AccountsFragment
import com.example.ds3companion.fragment.BossesFragment
import com.example.ds3companion.fragment.ChatFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.common.GooglePlayServicesUtil.isGooglePlayServicesAvailable
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

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
                    transaction.replace(R.id.fragmentContainer, ChatFragment()
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
            builder.setMessage("Do you want to exit App")
            builder.setPositiveButton("YES") { _, _ ->
                this.finish()
            }
            builder.setNegativeButton("NO") { dialog, _ ->
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
            return
        } else {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                println(it.latitude.toString())
                println(it.longitude.toString())
            }
        }
    }

}