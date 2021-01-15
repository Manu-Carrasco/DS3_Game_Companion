package com.example.ds3companion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.ds3companion.fragment.BossesFragment
import com.google.android.youtube.player.YouTubeStandalonePlayer

class ExtraInfoActivity : AppCompatActivity() {

    private lateinit var tittleText: TextView
    private lateinit var infoText: TextView
    private lateinit var videoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra_info)

        val numBoss = intent.getIntExtra("videoNum", 1)

        initViews(numBoss)
        initListeners(numBoss)
    }

    private fun initViews(bossNum: Int){
        tittleText = findViewById<TextView>(R.id.textTittle)
        infoText = findViewById<TextView>(R.id.textInfo)
        videoButton = findViewById<Button>(R.id.bossVideo)
        when(bossNum){
            1 -> {
                tittleText.text = getString(R.string.name_boss1)
                infoText.text = getString(R.string.info_boss1)
            }
            2 -> {
                tittleText.text = getString(R.string.name_boss2)
                infoText.text = getString(R.string.info_boss2)
            }
            3 -> {
                tittleText.text = getString(R.string.name_boss3)
                infoText.text = getString(R.string.info_boss3)
            }
            4 -> {
                tittleText.text = getString(R.string.name_boss4)
                infoText.text = getString(R.string.info_boss4)
            }
        }

    }
    private fun initListeners(bossNum: Int){
        videoButton.setOnClickListener{
            when(bossNum){
                1 -> {
                    val i = YouTubeStandalonePlayer.createVideoIntent(this, getString(R.string.api_key), getString(R.string.boss_video1))
                    startActivity(i)
                }
                2 -> {
                    val i = YouTubeStandalonePlayer.createVideoIntent(this, getString(R.string.api_key), getString(R.string.boss_video2))
                    startActivity(i)
                }
                3 -> {
                    val i = YouTubeStandalonePlayer.createVideoIntent(this, getString(R.string.api_key), getString(R.string.boss_video3))
                    startActivity(i)
                }
                4 -> {
                    val i = YouTubeStandalonePlayer.createVideoIntent(this, getString(R.string.api_key), getString(R.string.boss_video4))
                    startActivity(i)
                }
            }
        }
    }

}