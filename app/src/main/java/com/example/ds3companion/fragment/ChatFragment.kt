package com.example.ds3companion.fragment

import android.content.Context.LOCATION_SERVICE
import android.location.LocationManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ds3companion.Constants.COLLECTION_CHAT
import com.example.ds3companion.R
import com.example.ds3companion.adapter.ChatAdapter
import com.example.ds3companion.model.Chat
import com.example.ds3companion.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.grpc.Context
import java.util.*

class ChatFragment: Fragment(){

    private lateinit var tabsSound: MediaPlayer

    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button
    private  lateinit var auth: FirebaseAuth

    private  var db = Firebase.firestore
    private var name = "User"


    lateinit var  locationManager: LocationManager
    private var hasGPS = false
    private var hasNetwork = false

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

        initListeners()
    }

    private fun initViews(view: View) {
        view.findViewById<RecyclerView>(R.id.messagesRecylerView).layoutManager = LinearLayoutManager(requireContext())
        view.findViewById<RecyclerView>(R.id.messagesRecylerView).adapter = ChatAdapter(user = "user")

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


    private fun initListeners(){
        sendButton.setOnClickListener{
                sendMessage()
        }

    }
    private fun sendMessage(){
        val message = Chat(
                message = messageEditText.text.toString(),
                username =  name

        //Buscar en firebase users/UID/username
        )
        messageEditText.text.clear()

        db.collection("chat").document().set(message)
    }
}