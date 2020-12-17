package com.example.ds3companion.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
import java.util.*

class ChatFragment: Fragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var firestore: FirebaseFirestore

    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore =  Firebase.firestore
        initViews(view)
        initRecyclerView()
        getChats()                            /*___________________*/
        initListeners()                       /*___________________*/

    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        messageEditText = view.findViewById(R.id.messageEditText)         /*___________________*/
        sendButton = view.findViewById(R.id.sendButton)                   /*___________________*/
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)   /*___________________*/
    }

    private fun initRecyclerView(){
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        chatAdapter = ChatAdapter(chatList = listOf())  /*___________________*/
        recyclerView.adapter = chatAdapter
    }

    private fun initListeners(){
        sendButton.setOnClickListener{
            val message = messageEditText.text.toString()
            if(message.isBlank()) return@setOnClickListener
            sendMessage(message)
        }

        swipeRefreshLayout.setOnRefreshListener {
            getChats()
        }

    }

    private fun sendMessage(message: String){
        Firebase.auth.currentUser?.uid?.let { userId: String ->


            firestore
                    .collection(COLLECTION_CHAT)
                    .document(userId)
                    .get()
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            it.result?.toObject(User::class.java)?.let{user: User ->
                                val chat = Chat(
                                        userId = Firebase.auth.currentUser?.uid,
                                        message = message,
                                        sentAt = Date().time,
                                        isSent = false,
                                        imageUrl = null,
                                        username = user.username,
                                        avatarUrl = null // TODO: Support User Avatar
                                )
                                firestore
                                        .collection(COLLECTION_CHAT)
                                        .add(chat)
                                        .addOnCompleteListener{
                                            if(it.isSuccessful){
                                                Log.i("Chat","Success uploading message")
                                                getChats()
                                            }
                                            else{
                                                Log.i("Chat","Error uploading message")
                                                // TODO: Show error
                                            }
                                        }
                            } ?: run {
                                // TODO: Show error
                            }
                        } else {
                            // TODO: Show error
                        }
                    }
        } ?: run {
            // TODO: Show error
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