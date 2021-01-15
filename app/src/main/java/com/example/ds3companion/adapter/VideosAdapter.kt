package com.example.ds3companion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ds3companion.R
import com.example.ds3companion.YoutubeActivity
import com.example.ds3companion.model.Items
import com.example.ds3companion.model.YoutubeInfo
import com.google.android.youtube.player.YouTubeStandalonePlayer
import com.squareup.picasso.Picasso

class VideosAdapter(var videosList: YoutubeInfo, val apiKEY: String, val activity: YoutubeActivity): RecyclerView.Adapter<VideosAdapter.VideosViewHolder>() {

    override fun getItemCount(): Int {
        return videosList.items.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideosViewHolder(layoutInflater, Items())
    }

    override fun onBindViewHolder(holder: VideosAdapter.VideosViewHolder, position: Int) {
        val video = videosList.items[position]

        Picasso.get().load(video.snippet?.thumbnails?.medium?.url).into(holder.videoThumbnail)
        holder.tittleVideo.text = video.snippet?.title.toString()
        holder.videoChannel.text = video.snippet?.channelTitle.toString()
        holder.dateVideo.text = video.snippet?.publishedAt.toString()
        holder.videoClicked = video
    }

    inner class VideosViewHolder(view: View, var videoClicked: Items): RecyclerView.ViewHolder(view){

        val videoThumbnail: ImageView = view.findViewById(R.id.videoThumb)
        val tittleVideo: TextView = view.findViewById(R.id.videoTittle)
        val videoChannel: TextView = view.findViewById(R.id.videoChannel)
        val dateVideo: TextView = view.findViewById(R.id.videoTime)

        init {
            view.setOnClickListener{
                val i = YouTubeStandalonePlayer.createVideoIntent(activity, apiKEY, videoClicked.id?.videoId)
                view.context.startActivity(i)
            }
        }

    }
}