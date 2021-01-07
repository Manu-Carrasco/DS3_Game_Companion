package com.example.ds3companion

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.ds3companion.LoginActivity.Companion.RC_SIGN_IN
import com.example.ds3companion.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.roundToInt

class LoginActivity : AppCompatActivity() {

    private val MyTag = "LoginActivity"

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var gso: GoogleSignInOptions

    companion object{
        private const val RC_SIGN_IN = 120
    }

    private lateinit var googleButton: Button
    private lateinit var loginButton: Button
    private lateinit var forgotButton: Button
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

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun initViews() {
        loginButton = findViewById<Button>(R.id.loginButton)
        googleButton = findViewById<Button>(R.id.loginGoogleButton)
        forgotButton = findViewById<Button>(R.id.forgotPasswordButton)
        usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun initListeners() {
        val registerButton: Button = loginButton
        val googleButton: Button = googleButton
        val forgotButton: Button = forgotButton
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
        googleButton.setOnClickListener{
            signIn()
        }
        forgotButton.setOnClickListener{
//            val builder = AlertDialog.Builder(this)
//            builder.setTitle(getString(R.string.button_forgotPassword))
//            val view = layoutInflater.inflate(R.layout.dialog_forgot_password,null)
//            val username = view.findViewById<EditText>(R.id.et_username)
//            builder.setView(view)
//            builder.setPositiveButton("Reset", DialogInterface.OnClickListener{ _, _ ->
//                forgotPassword(username)
//            })
//            builder.setNegativeButton("close", DialogInterface.OnClickListener{ _, _ -> })
//            builder.show()
        }
    }

//    private fun forgotPassword(username: EditText){
//        if(username.text.toString().isEmpty()) {
//            return
//        }
//        if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
//            return
//        }
//        auth.sendPasswordResetEmail(username.text.toString())
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        showMessage("Message Send")
//                    }
//                }
//    }

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

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, LoginActivity.RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == LoginActivity.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if(task.isSuccessful){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(MyTag, "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(MyTag, "Google sign in failed", e)
                    // ...
                }
            } else {
                Log.w(MyTag, exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(MyTag, "signInWithCredential:success")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(MyTag, "signInWithCredential:failure", task.exception)
                }

                // ...
            }
    }

    private fun showMessage(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}