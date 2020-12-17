package com.example.ds3companion.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ds3companion.R
import com.example.ds3companion.RegisterActivity
import com.example.ds3companion.LoginActivity
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AccountsFragment: Fragment(){

    private lateinit var registerButton: Button
    private lateinit var loginButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_accounts, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListeners()
        saveData()
    }

    private fun initViews(parentView: View) {
        registerButton = parentView.findViewById<Button>(R.id.registerButton)
        loginButton = parentView.findViewById<Button>(R.id.loginButton)
    }

    private fun initListeners(){
        registerButton.setOnClickListener{
            Firebase.analytics.logEvent("registerButtonClick", null)
            val intent = Intent(activity, RegisterActivity::class.java)
            startActivity(intent)
        }
        loginButton.setOnClickListener{
            Firebase.analytics.logEvent("loginButtonClick", null)
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
    }



    private fun saveData(){
        val sharedPreferences = activity!!.getSharedPreferences("test", Context.MODE_PRIVATE)
        sharedPreferences.edit()
                .putString("firstkey", "value")
                .apply()
    }
}