package com.example.ds3companion.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ds3companion.R
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class BossesFragment: Fragment (){
    private lateinit var tabsSound: MediaPlayer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bosses, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Firebase.analytics.logEvent(getString(R.string.event_infoBosses), null)
        tabsSound = MediaPlayer.create(context, R.raw.accepteffect)
        tabsSound?.start()
    }

    override fun onResume() {
        super.onResume()


    }
}