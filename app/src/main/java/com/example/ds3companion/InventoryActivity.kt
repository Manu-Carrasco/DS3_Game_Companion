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
        getCharacterData("warrior")
    }

    fun getCharacterData(userClass:String){
        firestore.collection(Constants.COLLECTION_CLASSES).document(userClass).get().addOnSuccessListener { snapshot ->
            if(snapshot.exists()){
                //Obtenemos datos
                //char = snapshot.toObject(Character::class.java)
                findViewById<TextView>(R.id.nameText).text = snapshot.getString(R.string.class_name.toString())
                findViewById<TextView>(R.id.levelText).text = snapshot.getString(R.string.class_level.toString())
                findViewById<TextView>(R.id.locationText).text = snapshot.getString(R.string.class_location.toString())

                var seconds : Double? = snapshot.getString(R.string.class_playtime.toString())?.toDouble();
                var minutes : Double? = seconds?.div(60)
                var hours : Double? = minutes?.div(60)
                seconds = seconds?.rem(60)
                minutes = minutes?.rem(60);
                findViewById<TextView>(R.id.secondsText).text = "${seconds?.roundToInt()}"
                findViewById<TextView>(R.id.minutesText).text = "${minutes?.roundToInt()}"
                findViewById<TextView>(R.id.hoursText).text = "${hours?.roundToInt()}"
                Log.d(MyTag, "${seconds?.roundToInt()},${minutes?.roundToInt()}, ${hours?.roundToInt()} ")

            } else {
                showMessage(getString(R.string.error_serverDown))
            }
        }
    }

    private fun showMessage(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

}