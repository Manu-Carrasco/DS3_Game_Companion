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
                showMessage("Username or Password is not correct")
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
                        if(document.data.getValue("username") == username && document.data.getValue("password") == password) {
                            found = true;
                            email = document.data.getValue("email").toString()
                        }
                    }
                    if(found) {
                        // Show account characters
                        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                readUser()

                                showMessage("signInWithEmail:success")
                            } else {
                                showMessage("Authentication failed")
                            }
                            showMessage("User Logged")
                            progressBar.visibility = View.GONE
                            finish()
                        }
                    } else {
                        // Doesn't found player account
                        progressBar.visibility = View.GONE
                        showMessage("Username or Password is not correct")
                    }

                }
    }

    private fun readUser() {
        firestore.collection("users").document(auth.currentUser?.uid.toString()).get().addOnSuccessListener { snapshot ->
            if(snapshot.exists()){
                //Obtenemos datos
                getSharedPreferences("UserData", Context.MODE_PRIVATE).edit().clear().commit()
                getSharedPreferences("UserData", Context.MODE_PRIVATE).edit().putString("uid", auth.currentUser?.uid.toString()).apply()
                getSharedPreferences("UserData", Context.MODE_PRIVATE).edit().putString("username", snapshot.getString("username")).apply()
                getSharedPreferences("UserData", Context.MODE_PRIVATE).edit().putString("equipment", snapshot.getString("equipment")).apply()
                getSharedPreferences("UserData", Context.MODE_PRIVATE).edit().putString("level", snapshot.getString("level")).apply()
                getSharedPreferences("UserData", Context.MODE_PRIVATE).edit().putString("location", snapshot.getString("location")).apply()
                getSharedPreferences("UserData", Context.MODE_PRIVATE).edit().putString("playtime", snapshot.getString("playtime")).apply()
            } else {
                showMessage(getString(R.string.error_serverDown))
            }
        }
    }

    private fun showMessage(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}