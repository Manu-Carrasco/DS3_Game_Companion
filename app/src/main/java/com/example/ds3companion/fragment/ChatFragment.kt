package com.example.ds3companion.fragment

import android.content.Context.LOCATION_SERVICE
import android.location.LocationManager
import android.media.MediaPlayer
import android.os.Bundle
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.grpc.Context
import java.util.*

class ChatFragment: Fragment(){

    private val MyTag = "Chat"

    private lateinit var tabsSound: MediaPlayer

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var firestore: FirebaseFirestore

    private lateinit var chatAdapter: ChatAdapter

    lateinit var  locationManager: LocationManager
    private var hasGPS = false
    private var hasNetwork = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore =  Firebase.firestore
        initViews(view)
        initRecyclerView()

        tabsSound = MediaPlayer.create(context, R.raw.accepteffect)
        tabsSound?.start()

        getChats()
        initListeners()

    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        messageEditText = view.findViewById(R.id.messageEditText)
        sendButton = view.findViewById(R.id.sendButton)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
    }

    private fun initRecyclerView(){
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        chatAdapter = ChatAdapter(chatList = listOf())
        recyclerView.adapter = chatAdapter
    }

    private fun initListeners(){
        sendButton.setOnClickListener{
            val message = messageEditText.text.toString()
            if(message.isBlank()) return@setOnClickListener
//            sendMessage(message)
        }

        swipeRefreshLayout.setOnRefreshListener {
            getChats()
        }

    }

    private fun getChats(){
        swipeRefreshLayout.isRefreshing = true
        firestore
                .collection(COLLECTION_CHAT)
                .get()
                .addOnCompleteListener{
                    if (it.isSuccessful){
                        val chats: List<Chat> = it.result?.documents?.mapNotNull { it.toObject(Chat::class.java) }.orEmpty()
                        chatAdapter.chatList = chats
                        chatAdapter.notifyDataSetChanged()
                    } else{

                    }
                    swipeRefreshLayout.isRefreshing = false
                }
    }
}