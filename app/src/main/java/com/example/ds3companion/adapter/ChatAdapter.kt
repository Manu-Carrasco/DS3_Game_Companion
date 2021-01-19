package com.example.ds3companion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.ds3companion.R
import com.example.ds3companion.model.Chat

class ChatAdapter(private val user: String): RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    private var messages: List<Chat> = emptyList()

    fun setData(list: List<Chat>){
        messages = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_message,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

            holder.itemView.findViewById<TextView>(R.id.UsernameTextView).text = message.username
            holder.itemView.findViewById<TextView>(R.id.myMessageTextView).text = message.message

    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}