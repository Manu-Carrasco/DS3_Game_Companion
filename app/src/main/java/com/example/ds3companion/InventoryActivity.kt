package com.example.ds3companion

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.*
import com.example.ds3companion.model.Character
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.roundToInt



class InventoryActivity : AppCompatActivity() {
    private lateinit var tabsSound: MediaPlayer
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var chestButton: ImageButton
    private lateinit var headButton: ImageButton
    private lateinit var legsButton: ImageButton
    private lateinit var handsButton: ImageButton
    private lateinit var weapon1Button: ImageButton
    private lateinit var weapon2Button: ImageButton

    private lateinit var option1Button: ImageButton
    private lateinit var option2Button: ImageButton
    private lateinit var option3Button: ImageButton

    private lateinit var profileButton: ImageButton
    private lateinit var chosenPart: String

    private val MyTag = "Datos"
    private val REQUEST_IMAGE_CAPTURE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        initViews()
        initListeners()
        firestore = Firebase.firestore
        auth = Firebase.auth
        getCharacterData()
        val uid = getSharedPreferences(getString(R.string.class_userdata), Context.MODE_PRIVATE).getString(getString(R.string.class_uid), null)
        Log.d(MyTag, "$uid")
    }

    override fun onResume() {
        super.onResume()
        tabsSound = MediaPlayer.create(this, R.raw.openingeffect)
        tabsSound?.start()
        //getCharacterData()
    }
    private fun initViews() {
        headButton = findViewById<ImageButton>(R.id.headImage)
        chestButton = findViewById<ImageButton>(R.id.chestImage)
        legsButton = findViewById<ImageButton>(R.id.legsImage)
        handsButton = findViewById<ImageButton>(R.id.handsImage)
        weapon1Button = findViewById<ImageButton>(R.id.weapon1Image)
        weapon2Button = findViewById<ImageButton>(R.id.weapon2Image)

        option1Button = findViewById<ImageButton>(R.id.Option1Image)
        option2Button = findViewById<ImageButton>(R.id.Option2Image)
        option3Button = findViewById<ImageButton>(R.id.Option3Image)

        profileButton = findViewById<ImageButton>(R.id.characterImage)

    }

    private fun initListeners(){
        headButton.setOnClickListener {
            ViewOptions("head")
        }
        chestButton.setOnClickListener {
            ViewOptions("chest")
        }
        handsButton.setOnClickListener {
            ViewOptions("arms")
        }
        weapon1Button.setOnClickListener {
            ViewOptions("weapon1")
        }
        weapon2Button.setOnClickListener {
            ViewOptions("weapon2")
        }
        legsButton.setOnClickListener {
            ViewOptions("legs")
        }




        option1Button.setOnClickListener {
            ChangePart("1")
        }
        option2Button.setOnClickListener {
            ChangePart("2")
        }
        option3Button.setOnClickListener {
            ChangePart("3")
        }

        profileButton.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }
    fun getCharacterData(){
        var seconds : Double? = getSharedPreferences(getString(R.string.class_userdata), Context.MODE_PRIVATE).getString(getString(R.string.class_playtime), null)?.toDouble()
        var minutes : Double? = seconds?.div(60)
        var hours : Double? = minutes?.div(60)
        seconds = seconds?.rem(60)
        minutes = minutes?.rem(60)
        findViewById<TextView>(R.id.secondsText).text = "${seconds?.roundToInt()}"
        findViewById<TextView>(R.id.minutesText).text = "${minutes?.roundToInt()}"
        findViewById<TextView>(R.id.hoursText).text = "${hours?.roundToInt()}"

        firestore.collection(Constants.COLLECTION_USERS).document(auth.currentUser?.uid.toString()).get().addOnSuccessListener { snapshot ->
            if(snapshot.exists()){
                findViewById<TextView>(R.id.nameText).text = snapshot.getString(getString(R.string.sharedPreferences_username))
                findViewById<TextView>(R.id.levelText).text = snapshot.getString(getString(R.string.class_level))
                findViewById<TextView>(R.id.locationText).text = snapshot.getString(getString(R.string.class_location))

                ViewOptions(getString(R.string.class_head))
                snapshot.getString(getString(R.string.class_head))?.let { ChangePart(it) }
                ViewOptions(getString(R.string.class_arms))
                snapshot.getString(getString(R.string.class_arms))?.let { ChangePart(it) }
                ViewOptions(getString(R.string.class_chest))
                snapshot.getString(getString(R.string.class_chest))?.let { ChangePart(it) }
                ViewOptions(getString(R.string.class_weapon1))
                snapshot.getString(getString(R.string.class_weapon1))?.let { ChangePart(it) }
                ViewOptions(getString(R.string.class_weapon2))
                snapshot.getString(getString(R.string.class_weapon2))?.let { ChangePart(it) }
                ViewOptions(getString(R.string.class_legs))
                snapshot.getString(getString(R.string.class_legs))?.let { ChangePart(it) }

            }
        }

    }
    private fun ViewOptions(part: String){
        chosenPart = part
        when(part){
            "head" -> {
                    option1Button.visibility = View.VISIBLE
                    option1Button.setImageResource(R.drawable.ic_personaje1)
                    option2Button.visibility = View.VISIBLE
                    option2Button.setImageResource(R.drawable.ic_personaje2)
                    option3Button.visibility = View.VISIBLE
                    option3Button.setImageResource(R.drawable.ic_personaje3)
                    }
            "chest" -> { option1Button.visibility = View.VISIBLE
                option1Button.setImageResource(R.drawable.ic_personaje2)
                option2Button.visibility = View.VISIBLE
                option2Button.setImageResource(R.drawable.ic_personaje2)
                option3Button.visibility = View.VISIBLE
                option3Button.setImageResource(R.drawable.ic_personaje3)
            }
            "legs" -> { option1Button.visibility = View.VISIBLE
                option1Button.setImageResource(R.drawable.ic_personaje1)
                option2Button.visibility = View.VISIBLE
                option2Button.setImageResource(R.drawable.ic_personaje2)
                option3Button.visibility = View.VISIBLE
                option3Button.setImageResource(R.drawable.ic_personaje2)
            }
            "weapon1" -> { option1Button.visibility = View.VISIBLE
                option1Button.setImageResource(R.drawable.ic_personaje3)
                option2Button.visibility = View.VISIBLE
                option2Button.setImageResource(R.drawable.ic_personaje2)
                option3Button.visibility = View.VISIBLE
                option3Button.setImageResource(R.drawable.ic_personaje3)
            }
            "weapon2" -> { option1Button.visibility = View.VISIBLE
                option1Button.setImageResource(R.drawable.ic_personaje1)
                option2Button.visibility = View.VISIBLE
                option2Button.setImageResource(R.drawable.ic_personaje1)
                option3Button.visibility = View.VISIBLE
                option3Button.setImageResource(R.drawable.ic_personaje3)
            }
            "arms" -> { option1Button.visibility = View.VISIBLE
                option1Button.setImageResource(R.drawable.ic_personaje1)
                option2Button.visibility = View.VISIBLE
                option2Button.setImageResource(R.drawable.ic_personaje1)
                option3Button.visibility = View.VISIBLE
                option3Button.setImageResource(R.drawable.ic_personaje1)
            }
        }
    }
    private  fun ChangePart(option: String){
        when(chosenPart){
            "head" -> {
                when(option){
                    "1" -> headButton.setImageDrawable(option1Button.drawable)
                    "2" -> headButton.setImageDrawable(option2Button.drawable)
                    "3" -> headButton.setImageDrawable(option3Button.drawable)
                }
                firestore.collection(Constants.COLLECTION_USERS).document(auth.currentUser?.uid.toString()).update(getString(R.string.class_head), option)
            }
            "chest" -> {
                when(option){
                    "1" -> chestButton.setImageDrawable(option1Button.drawable)
                    "2" -> chestButton.setImageDrawable(option2Button.drawable)
                    "3" -> chestButton.setImageDrawable(option3Button.drawable)
                }
                firestore.collection(Constants.COLLECTION_USERS).document(auth.currentUser?.uid.toString()).update(getString(R.string.class_chest), option)
            }
            "legs" -> {
                when(option){
                    "1" -> legsButton.setImageDrawable(option1Button.drawable)
                    "2" -> legsButton.setImageDrawable(option2Button.drawable)
                    "3" -> legsButton.setImageDrawable(option3Button.drawable)
                }
                firestore.collection(Constants.COLLECTION_USERS).document(auth.currentUser?.uid.toString()).update(getString(R.string.class_legs), option)
            }
            "weapon1" -> {
                when(option){
                    "1" -> weapon1Button.setImageDrawable(option1Button.drawable)
                    "2" -> weapon1Button.setImageDrawable(option2Button.drawable)
                    "3" -> weapon1Button.setImageDrawable(option3Button.drawable)
                }
                firestore.collection(Constants.COLLECTION_USERS).document(auth.currentUser?.uid.toString()).update(getString(R.string.class_weapon1), option)
            }
            "weapon2" -> {
                when(option){
                    "1" -> weapon2Button.setImageDrawable(option1Button.drawable)
                    "2" -> weapon2Button.setImageDrawable(option2Button.drawable)
                    "3" -> weapon2Button.setImageDrawable(option3Button.drawable)
                }
                firestore.collection(Constants.COLLECTION_USERS).document(auth.currentUser?.uid.toString()).update(getString(R.string.class_weapon2), option)
            }
            "arms" -> {
                when(option){
                    "1" -> handsButton.setImageDrawable(option1Button.drawable)
                    "2" -> handsButton.setImageDrawable(option2Button.drawable)
                    "3" -> handsButton.setImageDrawable(option3Button.drawable)
                }
                firestore.collection(Constants.COLLECTION_USERS).document(auth.currentUser?.uid.toString()).update(getString(R.string.class_arms), option)
            }
        }
        chosenPart = ""
        CloseOptions()
    }
    private fun CloseOptions(){
        option1Button.visibility = View.GONE
        option2Button.visibility = View.GONE
        option3Button.visibility = View.GONE
    }
    private fun showMessage(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            (data?.extras?.get("data") as? Bitmap)?.let { bitmap -> profileButton.setImageBitmap(bitmap) }

        }
    }


}