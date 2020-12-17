package com.example.ds3companion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ds3companion.R
import com.example.ds3companion.model.Chat

class ChatAdapter(var chatList: List<Chat>): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder{
        val chatView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(chatView)
    }

    override fun onBindViewHolder(holder: ChatAdapter.ChatViewHolder, position: Int) {
        val chat = chatList[position]
        holder.messageTextView.text = chat.message
        holder.usernameTextView.text = chat.username
        holder.dateTextView.text = chat.sentAt.toString()
    }

    override fun getItemCount(): Int {
        return chatList.count()
    }

    inner class ChatViewHolder(view: View): RecyclerView.ViewHolder(view){
        val messageTextView: TextView = view.findViewById(R.id.messageTextView)
        val usernameTextView: TextView = view.findViewById(R.id.usernameTextView)
        val dateTextView: TextView = view.findViewById(R.id.messageDateTextView)
    }

}