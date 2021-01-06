package com.example.ds3companion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ds3companion.model.User
import com.google.android.gms.auth.api.phone.SmsRetriever.getClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.safetynet.SafetyNet.getClient
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private val MinPasswordLenght = 6
    private val MyTag = "RegisterActivity"

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore



    private lateinit var registerButton: Button
    private lateinit var googleButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initViews()
        initListeners()

        auth = Firebase.auth
        firestore = Firebase.firestore
    }

    private fun initViews(){
        registerButton = findViewById<Button>(R.id.registerButton)
        googleButton = findViewById<Button>(R.id.registerGoogleButton)
        emailEditText = findViewById<EditText>(R.id.emailEditText)
        passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun initListeners(){
        val registerButton: Button = registerButton
        val googleButton: Button = googleButton
        registerButton.setOnClickListener{

           val username = usernameEditText.text.toString()
            if(username.isEmpty()){
                usernameEditText.error = getString(R.string.hint_enter_username)
                return@setOnClickListener
            }

            val email = emailEditText.text.toString()
            if(!isEmailValid(email)){
                emailEditText.error = getString(R.string.error_email_invalid)
                return@setOnClickListener
            }

            val password = passwordEditText.text.toString()
            if(!isPasswordValid(password)){
                passwordEditText.error = getString(R.string.error_password_invalid, MinPasswordLenght)
                return@setOnClickListener
            }

            registerUser(email, password, username)
        }
        googleButton.setOnClickListener{
            //signIn()
        }
    }

    private fun registerUser(email: String, password: String, username: String){
        progressBar.visibility = View.VISIBLE
        registerButton.isEnabled = false

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful){
                        auth.currentUser?.uid?.let{ userId ->
                            val aux = (0 until 4).random()
                            var inventory = ""
                            when(aux){
                                1 -> inventory = getString(R.string.userclass_class1)
                                2 -> inventory = getString(R.string.userclass_class2)
                                3 -> inventory = getString(R.string.userclass_class3)
                            }

                            val user = User(userId = userId, username = username, password = password, equipment = inventory, email = email)
                            firestore
                                    .collection(Constants.COLLECTION_USERS)
                                    .document(userId)
                                    .set(user)
                                    .addOnCompleteListener{
                                        progressBar.visibility = View.GONE
                                        finish()
                                        if (it.isSuccessful){
                                            showMessage(getString(R.string.message_userCreated))
                                            Firebase.analytics.logEvent(getString(R.string.event_registerCorrect), null)
                                        } else {
                                            showMessage(getString(R.string.error_signUp))
                                        }
                                    }
                        } ?: kotlin.run {
                            showMessage(getString(R.string.error_signUp))
                            progressBar.visibility = View.GONE
                            registerButton.isEnabled = true
                        }
                    }
                    else{
                        showMessage(getString(R.string.error_signUp))
                        progressBar.visibility = View.GONE
                        registerButton.isEnabled = true
                    }
        }
    }

    private fun isEmailValid(email: String): Boolean{
        val emailRegex = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"



        return email.isNotBlank() && email.contains("@") && email.contains(Regex(emailRegex))
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.isNotBlank() && password.count() >= MinPasswordLenght  && containsLetterAndNumber(password)
    }

    private fun containsLetterAndNumber(text: String): Boolean {
        var containsLetter = false
        var containsNumber = false
        text.forEach{
            if(it.isDigit()){
                containsNumber = true
            }
            if(it.isLetter()){
                containsLetter = true
            }
        }
        return (containsLetter && containsNumber)
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun showMessage(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}