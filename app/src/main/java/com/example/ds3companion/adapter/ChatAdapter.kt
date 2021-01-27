package com.example.ds3companion.adapter

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings.System.putString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ds3companion.Activity_map
import com.example.ds3companion.MainActivity
import com.example.ds3companion.R
import com.example.ds3companion.fusedLocationProviderClient
import com.example.ds3companion.model.Chat
import java.lang.reflect.Array.getDouble
import java.lang.reflect.Array.getFloat
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class ChatAdapter(private val user: String, private val myLocation: String, private val mainReference: MainActivity): RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    private var messages: List<Chat> = emptyList()
    private val ownLocation = myLocation
    private val activityReference = mainReference

    fun setData(list: List<Chat>){
        messages = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(LayoutInflater.from(parent.context).inflate( R.layout.item_message,parent,false), ownLocation, activityReference)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

            holder.itemView.findViewById<TextView>(R.id.UsernameTextView).text = message.username
            holder.itemView.findViewById<TextView>(R.id.myMessageTextView).text = message.message
            holder.itemView.findViewById<TextView>(R.id.myLocationText).text = message.location
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class MessageViewHolder(itemView: View, var ownLocation: String, activityReference: MainActivity): RecyclerView.ViewHolder(itemView){
        init {
            itemView.setOnClickListener{
                if(ActivityCompat.checkSelfPermission(activityReference, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(activityReference, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
                    Toast.makeText(itemView.context, "We need your location to calculate distances", Toast.LENGTH_LONG).show()
                } else {
                    if(ownLocation.isEmpty()) {
                        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                            ownLocation = it.latitude.toString() + "," + it.longitude.toString();
                        }
                    }
                    if(itemView.findViewById<TextView>(R.id.myLocationText).text == " ") {
                        Toast.makeText(itemView.context, "User's location not public", Toast.LENGTH_LONG).show()
                    } else {
                        val textLocation = itemView.findViewById<TextView>(R.id.myLocationText).text.toString()
                        val distance = calculateDistance(ownLocation, textLocation)
                        val ping = calculatePing(distance)
                        val info = "Distance: $distance km | Minimal Ping: $ping ms"
                        val locationX = textLocation.split(",")[0].toDouble()
                        val locationY = textLocation.split(",")[1].toDouble()

                        // This way a new Activity can be created from the Adapter,
                        // it needs the Bundle in order to get the info from the intent
                        val intent = Intent(activityReference, Activity_map::class.java)
                        val extras = Bundle()
                        //extras.putDouble("locationX", locationX)
                        //extras.putDouble("locationY", locationY)
                        extras.putDouble("locationX", 35.689487)
                        extras.putDouble("locationY", 139.691711)
                        extras.putString("info", info)
                        intent.putExtras(extras)
                        startActivity(activityReference, intent, extras)
                    }
                }
            }
        }

        private fun calculateDistance(ownLocation: String, textLocation: String): Float {
            // This is a formula based on different sources from internet
            val earthRadiusKm = 6371;
            val delimiter = ","
            val ownLatitude = ownLocation.split(delimiter)[0].toFloat()
            val ownLongitute = ownLocation.split(delimiter)[1].toFloat()
            //val userLatitude = textLocation.split(delimiter)[0].toFloat()     // User Message Clicked Coordinates
            //val userLongitute = textLocation.split(delimiter)[1].toFloat()    // User Message Clicked Coordinates
            val userLatitude = 35.689487    // Tokyo Coordinates
            val userLongitute = 139.691711  // Tokyo Coordinates
            val alpha1 = ownLatitude * Math.PI/180
            val alpha2 = userLatitude * Math.PI/180
            val alphaIncrement = (userLatitude - ownLatitude) * Math.PI/180
            val lamdaIncrement = (userLongitute - ownLongitute) * Math.PI/180
            val a = sin(alphaIncrement/2) * sin(alphaIncrement/2) +
                    cos(alpha1) * cos(alpha2) *
                    sin(lamdaIncrement/2) * sin(lamdaIncrement/2);
            val distance = 2 * atan2(sqrt(a), sqrt(1-a));
            return (distance * earthRadiusKm).toFloat();
        }

        private fun calculatePing(distance: Float): Float {
            val pingCostPerKm = 0.01757 // Constant based on different sources from internet
            return (distance * pingCostPerKm).toFloat()
        }
    }
}