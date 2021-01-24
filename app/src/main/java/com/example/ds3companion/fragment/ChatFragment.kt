package com.example.ds3companion.fragment

import android.location.LocationManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ds3companion.MainActivity
import com.example.ds3companion.R
import com.example.ds3companion.adapter.ChatAdapter
import com.example.ds3companion.model.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatFragment(mainActivity: MainActivity, locationPermission: String) : Fragment(){

    private lateinit var tabsSound: MediaPlayer

    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button
    private  lateinit var auth: FirebaseAuth

    private  var db = Firebase.firestore
    private var name = "User"

    private val mainReference = mainActivity
    lateinit var  locationManager: LocationManager
    private var hasGPS = false
    private var hasNetwork = false
    private val gpsLocation = locationPermission

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)

        auth = Firebase.auth
        db.collection("users").document(auth.currentUser?.uid.toString()).get().addOnSuccessListener { user ->
            name = user.getString("username").toString()
        }
        tabsSound = MediaPlayer.create(context, R.raw.accepteffect)
        tabsSound?.start()

        initListeners(view)
    }

    private fun initViews(view: View) {
        view.findViewById<RecyclerView>(R.id.messagesRecylerView).layoutManager = LinearLayoutManager(requireContext())
        view.findViewById<RecyclerView>(R.id.messagesRecylerView).adapter = ChatAdapter(user = "user", gpsLocation, mainReference)

        messageEditText = view.findViewById(R.id.messageTextField)
        sendButton = view.findViewById(R.id.sendMessageButton)

        //swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
    db.collection("chat").orderBy("time", Query.Direction.ASCENDING).get().addOnSuccessListener { messages ->
            val listMessages = messages.toObjects(Chat::class.java)
            (view.findViewById<RecyclerView>(R.id.messagesRecylerView).adapter as ChatAdapter).setData(listMessages)
        }

        db.collection("chat").orderBy("time", Query.Direction.ASCENDING).addSnapshotListener { messages, error ->
            if(error == null){
                messages?.let{
                    val listMessages = it.toObjects(Chat::class.java)
                    (view.findViewById<RecyclerView>(R.id.messagesRecylerView).adapter as ChatAdapter).setData(listMessages)
                }
            }
        }
    }


    private fun initListeners(view: View){
        sendButton.setOnClickListener{
                sendMessage(view)
        }

    }
    private fun sendMessage(view: View){
        val message = Chat(
                message = messageEditText.text.toString(),
                username =  name,
                location = gpsLocation
                //Buscar en firebase users/UID/username
        )
        messageEditText.text.clear()
        db.collection("chat").document().set(message)
    }
}