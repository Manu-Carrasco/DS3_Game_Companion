package com.example.ds3companion.adapter

import android.annotation.SuppressLint
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ds3companion.R
import com.example.ds3companion.YoutubeActivity
import com.example.ds3companion.model.ErrorFound
import com.example.ds3companion.model.Items
import com.example.ds3companion.model.YoutubeInfo
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class ErrorAdapter(var errorList: ErrorFound): RecyclerView.Adapter<ErrorAdapter.ErrorsViewHolder>()  {

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ErrorsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.item_error, parent, false)
        return ErrorsViewHolder(layoutInflater)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ErrorsViewHolder, position: Int) {
        val errorType: String = errorList.error?.message.toString()
        holder.textCode.text = "Error: " + errorList.error?.code.toString()
        holder.textMessage.text = errorType
        Firebase.analytics.logEvent(errorType, null)
    }

    inner class ErrorsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var textCode: TextView = view.findViewById(R.id.codeError)
        var textMessage: TextView = view.findViewById(R.id.messageError)
    }
}