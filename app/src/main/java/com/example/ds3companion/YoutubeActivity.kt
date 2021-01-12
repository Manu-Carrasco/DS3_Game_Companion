package com.example.ds3companion

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.youtube.player.*

class YoutubeActivity : YouTubeBaseActivity() {

    private lateinit var  youtubePlayerInit: YouTubePlayer.OnInitializedListener
    private lateinit var playerView: YouTubePlayerView
    private val apiKey = R.string.api_key
    private val videoID = "U6JX2jUKHhI"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)
        initUI()
    }

//    private fun initUI() {
//        youtubePlayerInit = object :YouTubePlayer.OnInitializedListener{
//            override fun onInitializationSuccess( p0: YouTubePlayer.Provider?, youtubePLayer: YouTubePlayer?,  p2: Boolean ) {
//                youtubePLayer?.loadVideo(videoID)
//            }
//
//            override fun onInitializationFailure( p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult? ) {
//                Toast.makeText(applicationContext, "Algo fue mal", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun initUI() {
        playerView = findViewById(R.id.youtube_fragment)
        playerView.initialize(getString(R.string.api_key), object:YouTubePlayer.OnInitializedListener{

            override fun onInitializationSuccess( provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean ) {
                if(player == null) { return }
                if(wasRestored) { player.play() }
                else {
                    player.cueVideo(videoID)
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                }
            }

            override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult? ) {
                Toast.makeText(applicationContext, p1.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

//    private fun initUI() {
//        val youTubePlayerFragment = supportFragmentManager.findFragmentById(R.id.youtube_fragment)
//                as YouTubePlayerSupportFragment
//            youTubePlayerFragment.initialize(getString(apiKey), object : YouTubePlayer.OnInitializedListener {
//                override fun onInitializationSuccess( provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean ) {
//                    if (player == null) {
//                        return
//                    }
//                    if (wasRestored) {
//                        player.play()
//                    } else {
//                        player.cueVideo(videoID)
//                        player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
//                    }
//                }
//
//                override fun onInitializationFailure( p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult? ) {
//                    Toast.makeText(applicationContext, p1.toString(), Toast.LENGTH_SHORT).show()
//                }
//            })
//    }




}



