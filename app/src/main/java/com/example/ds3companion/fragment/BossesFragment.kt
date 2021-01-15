package com.example.ds3companion.fragment

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.ds3companion.ExtraInfoActivity
import com.example.ds3companion.R
import com.example.ds3companion.YoutubeActivity
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class BossesFragment: Fragment (){
    private lateinit var tabsSound: MediaPlayer
    private lateinit var buttonGuides: Button
    private lateinit var buttonBoss1: Button
    private lateinit var buttonBoss2: Button
    private lateinit var buttonBoss3: Button
    private lateinit var buttonBoss4: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bosses, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonGuides = view.findViewById<Button>(R.id.buttonGuides)
        buttonBoss1 = view.findViewById<Button>(R.id.boss1Button)
        buttonBoss2 = view.findViewById<Button>(R.id.boss2Button)
        buttonBoss3 = view.findViewById<Button>(R.id.boss3Button)
        buttonBoss4 = view.findViewById<Button>(R.id.boss4Button)


        buttonGuides.setOnClickListener{
            val intent = Intent(activity, YoutubeActivity::class.java)
            startActivity(intent)
        }

        buttonBoss1.setOnClickListener{
            val intent = Intent(view.context, ExtraInfoActivity::class.java).putExtra("videoNum", 1)
            startActivity(intent)
        }
        buttonBoss2.setOnClickListener{
            val intent = Intent(view.context, ExtraInfoActivity::class.java).putExtra("videoNum", 2)
            startActivity(intent)
        }
        buttonBoss3.setOnClickListener{
            val intent = Intent(view.context, ExtraInfoActivity::class.java).putExtra("videoNum", 3)
            startActivity(intent)
        }
        buttonBoss4.setOnClickListener{
            val intent = Intent(view.context, ExtraInfoActivity::class.java).putExtra("videoNum", 4)
            startActivity(intent)
        }

        Firebase.analytics.logEvent(getString(R.string.event_infoBosses), null)
        tabsSound = MediaPlayer.create(context, R.raw.accepteffect)
        tabsSound.start()
    }
}