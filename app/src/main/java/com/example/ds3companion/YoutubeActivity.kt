package com.example.ds3companion

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ds3companion.adapter.ErrorAdapter
import com.example.ds3companion.adapter.VideosAdapter
import com.example.ds3companion.model.ErrorFound
import com.example.ds3companion.model.Id
import com.example.ds3companion.model.Items
import com.example.ds3companion.model.YoutubeInfo
import com.google.android.gms.common.GooglePlayServicesUtil.getErrorDialog
import com.google.android.youtube.player.*
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_youtube.*
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import okhttp3.*
import ru.gildor.coroutines.okhttp.await
import java.io.IOException
import kotlin.coroutines.coroutineContext

// class AndroidFlavourAdapter: RecyclerView.Adapter<AndroidFlavourAdapter.FlavourViewHolder>() {}

class YoutubeActivity : YouTubeBaseActivity() {


    private lateinit var lifecycleOwner: LifecycleOwner

    private var dateJson: String? = null
    private var viewJson: String? = null
    private var relevanceJson: String? = null

    private lateinit var dateButton: Button
    private lateinit var viewButton: Button
    private lateinit var relevantButton: Button

    private val dateURL = "https://youtube.googleapis.com/youtube/v3/search?part=snippet&maxResults=25&order=date&publishedAfter=2020-01-01T00%3A00%3A00Z&q=dark%20souls%203&relevanceLanguage=en&type=video&key=AIzaSyBrq9T8zNPKvwrMqvVDcvAL92h0Ps2f13k"
    private val viewsURL = "https://youtube.googleapis.com/youtube/v3/search?part=snippet&maxResults=25&order=viewCount&q=dark%20souls%203&relevanceLanguage=en&type=video&key=AIzaSyBrq9T8zNPKvwrMqvVDcvAL92h0Ps2f13k_"
    private val relevanceURL = "https://youtube.googleapis.com/youtube/v3/search?part=snippet&maxResults=25&order=relevance&q=dark%20souls%203&relevanceLanguage=en&type=video&key=AIzaSyBrq9T8zNPKvwrMqvVDcvAL92h0Ps2f13k"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)
        recyclerViewVideos.layoutManager = LinearLayoutManager(this)
        initViews()
        initListeners()
    }

    private fun initViews(){
        dateButton = findViewById<Button>(R.id.dateButton)
        viewButton = findViewById<Button>(R.id.viewsButton)
        relevantButton = findViewById<Button>(R.id.relevantButton)
    }

    private fun initListeners(){
        val dateVideos: Button = dateButton
        val viewVideos: Button = viewButton
        val relevantVideos: Button = relevantButton

        dateVideos.setOnClickListener{
            if(dateJson == null){
                getDateGson()
            } else {
                getYoutubeInfo(dateJson!!)
            }
        }

        viewVideos.setOnClickListener{
            if(viewJson == null){
                getViewGson()
            } else {
                getYoutubeInfo(viewJson!!)
            }
        }

        relevantVideos.setOnClickListener{
            if(relevanceJson == null){
                getRelevanceGson()
            } else {
                getYoutubeInfo(relevanceJson!!)
            }
        }
    }

    private fun getYoutubeInfo(jsonInfo: String){
        val gson = GsonBuilder().create()
        val youtubeInfo = gson.fromJson(jsonInfo, YoutubeInfo::class.java)
        if (youtubeInfo.items.count() == 0) {
            val errorInfo = gson.fromJson(jsonInfo, ErrorFound::class.java)
            runOnUiThread{
                recyclerViewVideos.adapter = ErrorAdapter(errorInfo)
            }
        } else {
            runOnUiThread{
                recyclerViewVideos.adapter = VideosAdapter(youtubeInfo, getString(R.string.api_key), this@YoutubeActivity)
            }
        }
    }

    private fun getDateGson(){
        val client = OkHttpClient()
        try {
            GlobalScope.launch {
                withContext(Dispatchers.IO){
                    val dateRequest = Request.Builder().url(dateURL).build()
                    client.newCall(dateRequest).enqueue(object: Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            showMessage(getString(R.string.error_youtubeConnect))
                            println(e)
                        }
                        override fun onResponse(call: Call, response: Response) {
                            dateJson = response.body?.string().toString()
                            getYoutubeInfo(dateJson!!)
                        }
                    })
                }
            }
        } catch (e: IOException) {
            showMessage(getString(R.string.error_youtubeConnect))
            println(e)
            throw IOException("Some additional debug info: ${e.message}", e)
        }
    }

    private fun getViewGson(){

        val client = OkHttpClient()
        val dateRequest = Request.Builder().url(viewsURL).build()
        client.newCall(dateRequest).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                //println("Algo ha ido mal al intentar sacar la info de youtube")
            }
            override fun onResponse(call: Call, response: Response) {
                viewJson = response.body?.string().toString()
                getYoutubeInfo(viewJson!!)
            }
        })

    }

    private fun getRelevanceGson(){

        val client = OkHttpClient()
        val dateRequest = Request.Builder().url(relevanceURL).build()
        client.newCall(dateRequest).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                //println("Algo ha ido mal al intentar sacar la info de youtube")
            }
            override fun onResponse(call: Call, response: Response) {
                relevanceJson = response.body?.string().toString()
                getYoutubeInfo(relevanceJson!!)
            }
        })

    }

    private fun showMessage(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}



