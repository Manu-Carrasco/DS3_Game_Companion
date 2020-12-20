package com.example.ds3companion

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.ds3companion.model.Character
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.roundToInt

private lateinit var firestore: FirebaseFirestore
var char: Character? = null

class InventoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)
        firestore = Firebase.firestore
        getCharacterData("warrior")
    }

    fun getCharacterData(userClass:String){
        firestore.collection("classes").document(userClass).get().addOnSuccessListener { snapshot ->
            if(snapshot.exists()){
                //Obtenemos datos
                //char = snapshot.toObject(Character::class.java)
                findViewById<TextView>(R.id.nameText).text = snapshot.getString("name")
                findViewById<TextView>(R.id.levelText).text = snapshot.getString("level")
                findViewById<TextView>(R.id.locationText).text = snapshot.getString("location")

                var seconds : Double? = snapshot.getString("playTime")?.toDouble();
                var minutes : Double? = seconds?.div(60)
                var hours : Double? = minutes?.div(60)
                seconds = seconds?.rem(60)
                minutes = minutes?.rem(60);
                findViewById<TextView>(R.id.secondsText).text = "${seconds?.roundToInt()}"
                findViewById<TextView>(R.id.minutesText).text = "${minutes?.roundToInt()}"
                findViewById<TextView>(R.id.hoursText).text = "${hours?.roundToInt()}"
                Log.d("datos", "${seconds?.roundToInt()},${minutes?.roundToInt()}, ${hours?.roundToInt()} ")

            }else{
                //No existe
            }
        }
    }

}