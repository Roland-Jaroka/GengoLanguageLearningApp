package com.example.gengolearning.model

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object AnalyticsHelper {
    private lateinit var analytics: FirebaseAnalytics

    fun init(context: Context) {
        analytics = FirebaseAnalytics.getInstance(context)
    }
    fun logEvent (name: String, params: Bundle? = null) {

        analytics.logEvent(name, params)
    }

}