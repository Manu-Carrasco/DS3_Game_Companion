package com.example.ds3companion.fragment

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ds3companion.InventoryActivity
import com.example.ds3companion.R
import com.example.ds3companion.RegisterActivity
import com.example.ds3companion.LoginActivity
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AccountsFragment: Fragment(){

    private lateinit var registerButton: Button
    private lateinit var loginButton: Button
    private lateinit var logoutButton: Button
    private lateinit var welcomeText: TextView
    private lateinit var loginText: TextView
    private lateinit var registerText: TextView

    private lateinit var tabsSound: MediaPlayer

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_accounts, container,false)
    }

//    override fun onResume() {
//        super.onResume()
//        checkUserAvailability()
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListeners()
        saveData()

        tabsSound = MediaPlayer.create(context, R.raw.accepteffect)
        tabsSound?.start()

        auth = Firebase.auth
        firestore = Firebase.firestore
        checkUserAvailability()
    }

    private fun initViews(parentView: View) {
        registerButton = parentView.findViewById<Button>(R.id.registerButton)
        loginButton = parentView.findViewById<Button>(R.id.loginButton)
        logoutButton = parentView.findViewById<Button>(R.id.logOutButton)
        welcomeText = parentView.findViewById<TextView>(R.id.welcomeText)
        loginText = parentView.findViewById<TextView>(R.id.loginText)
        registerText = parentView.findViewById<TextView>(R.id.registerText)
    }

    private fun initListeners(){
        registerButton.setOnClickListener{
            Firebase.analytics.logEvent(getString(R.string.event_registerClick), null)
            val intent = Intent(activity, RegisterActivity::class.java)
            startActivity(intent)
        }
        loginButton.setOnClickListener{
            Firebase.analytics.logEvent(getString(R.string.event_loginClick), null)
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
        logoutButton.setOnClickListener{
            auth.signOut()
            checkUserAvailability()
        }
    }


    private fun checkUserAvailability() {
        if(auth.currentUser != null) {
            // Local account found
            registerButton.visibility = View.GONE
            loginButton.visibility = View.GONE
            loginText.visibility = View.GONE
            registerText.visibility = View.GONE
            logoutButton.visibility = View.VISIBLE
            welcomeText.visibility = View.VISIBLE
            val intent = Intent(activity, InventoryActivity::class.java)
            startActivity(intent)
        } else {
            registerButton.visibility = View.VISIBLE
            loginButton.visibility = View.VISIBLE
            loginText.visibility = View.VISIBLE
            registerText.visibility = View.VISIBLE
            logoutButton.visibility = View.GONE
            welcomeText.visibility = View.GONE
        }
    }

    private fun saveData(){
        val sharedPreferences = activity!!.getSharedPreferences(getString(R.string.sharedPreferences_name), Context.MODE_PRIVATE)
        sharedPreferences.edit()
                .putString(getString(R.string.sharedPreferences_string1), getString(R.string.sharedPreferences_string2))
                .apply()
    }
}