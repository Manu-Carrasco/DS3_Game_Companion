package com.example.ds3companion

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.ds3companion.model.Character
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.roundToInt



class InventoryActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val MyTag = "Datos"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)
        firestore = Firebase.firestore
        auth = Firebase.auth
        getCharacterData()
        val uid = getSharedPreferences(getString(R.string.class_userdata), Context.MODE_PRIVATE).getString(getString(R.string.class_uid), null)
        Log.d(MyTag, "$uid")
    }

    fun getCharacterData(){
        findViewById<TextView>(R.id.nameText).text = getSharedPreferences(getString(R.string.class_userdata), Context.MODE_PRIVATE).getString(getString(R.string.sharedPreferences_username), null)
        findViewById<TextView>(R.id.levelText).text = getSharedPreferences(getString(R.string.class_userdata), Context.MODE_PRIVATE).getString(getString(R.string.class_level), null)
        findViewById<TextView>(R.id.locationText).text = getSharedPreferences(getString(R.string.class_userdata), Context.MODE_PRIVATE).getString(getString(R.string.class_location), null)
        var seconds : Double? = getSharedPreferences(getString(R.string.class_userdata), Context.MODE_PRIVATE).getString(getString(R.string.class_playtime), null)?.toDouble()
        var minutes : Double? = seconds?.div(60)
        var hours : Double? = minutes?.div(60)
        seconds = seconds?.rem(60)
        minutes = minutes?.rem(60);
        findViewById<TextView>(R.id.secondsText).text = "${seconds?.roundToInt()}"
        findViewById<TextView>(R.id.minutesText).text = "${minutes?.roundToInt()}"
        findViewById<TextView>(R.id.hoursText).text = "${hours?.roundToInt()}"
    }

    private fun showMessage(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

}