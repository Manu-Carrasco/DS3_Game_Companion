package com.example.ds3companion

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import com.example.ds3companion.model.User
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.roundToInt

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var loginButton: Button
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        initViews()
        initListeners()

        auth = Firebase.auth
        firestore = Firebase.firestore
    }

    private fun initViews() {
        loginButton = findViewById<Button>(R.id.loginButton)
        usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun initListeners() {
        val registerButton: Button = loginButton
        registerButton.setOnClickListener{

            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            if(username.isEmpty() || password.isEmpty()){
                usernameEditText.error = getString(R.string.hint_enter_username)
                passwordEditText.error = getString(R.string.hint_enter_password)
                showMessage(getString(R.string.error_email_or_password))
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE

            findOnlineAccount(username, password)
        }
    }

    private fun findOnlineAccount(username: String, password: String){
        firestore
                .collection(Constants.COLLECTION_USERS)
                .get()
                .addOnCompleteListener{
                    var found = false
                    var email = ""
                    for(document in it.result!!){
                        if(document.data.getValue(getString(R.string.sharedPreferences_username)) == username && document.data.getValue(getString(R.string.sharedPreferences_password)) == password) {
                            found = true;
                            email = document.data.getValue(getString(R.string.sharedPreferences_email)).toString()
                        }
                    }
                    if(found) {
                        // Show account characters
                        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Firebase.analytics.logEvent(getString(R.string.event_loginCorrect), null)
                                readUser()
                                showMessage(getString(R.string.message_userLogged))
                            } else {
                                showMessage(getString(R.string.error_logging))
                            }
                            progressBar.visibility = View.GONE
                            finish()
                        }
                    } else {
                        // Doesn't found player account
                        progressBar.visibility = View.GONE
                        showMessage(getString(R.string.error_email_or_password))
                    }

                }
    }

    private fun readUser() {
        firestore.collection(Constants.COLLECTION_USERS).document(auth.currentUser?.uid.toString()).get().addOnSuccessListener { snapshot ->
            if(snapshot.exists()){
                //Obtenemos datos
                getSharedPreferences(getString(R.string.class_userdata), Context.MODE_PRIVATE).edit().clear().commit()
                getSharedPreferences(getString(R.string.class_userdata), Context.MODE_PRIVATE).edit().putString(getString(R.string.class_uid), auth.currentUser?.uid.toString()).apply()
                getSharedPreferences(getString(R.string.class_userdata), Context.MODE_PRIVATE).edit().putString(getString(R.string.sharedPreferences_username), snapshot.getString(getString(R.string.sharedPreferences_username))).apply()
                getSharedPreferences(getString(R.string.class_userdata), Context.MODE_PRIVATE).edit().putString(getString(R.string.class_equipment), snapshot.getString(getString(R.string.class_equipment))).apply()
                getSharedPreferences(getString(R.string.class_userdata), Context.MODE_PRIVATE).edit().putString(getString(R.string.class_level), snapshot.getString(getString(R.string.class_level))).apply()
                getSharedPreferences(getString(R.string.class_userdata), Context.MODE_PRIVATE).edit().putString(getString(R.string.class_location), snapshot.getString(getString(R.string.class_location))).apply()
                getSharedPreferences(getString(R.string.class_userdata), Context.MODE_PRIVATE).edit().putString(getString(R.string.class_playtime), snapshot.getString(getString(R.string.class_playtime))).apply()
            } else {
                showMessage(getString(R.string.error_serverDown))
            }
        }
    }

    private fun showMessage(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}