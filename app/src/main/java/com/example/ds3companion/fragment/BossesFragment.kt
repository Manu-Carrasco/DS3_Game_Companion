package com.example.ds3companion.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ds3companion.R
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class BossesFragment: Fragment (){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bosses, container,false)
        Firebase.analytics.logEvent(getString(R.string.event_infoBosses), null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}