package com.example.gengolearning.uielements.dashboard.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.model.AppSettingsPreferences
import kotlinx.coroutines.launch

class SettingsViewModel: ViewModel(){

    fun sendFeedback(context: Context){
        val recipient = "jaroka.roland@gmail.com"
        val subject= "Feedback"
        val body = """"
            Dear Gen-Go app team
            
            I have the following suggestion to the app/ I experienced the following bug
            
            
            
            """.trimIndent()

        val intent= Intent(Intent.ACTION_SENDTO).apply{
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        context.startActivity(Intent.createChooser(intent,"Send feedback"))

    }

    fun openAppLanguages(context: Context){
        val intent= Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent, null)
    }

    fun clearUserPreferences(context: Context){
        viewModelScope.launch {
            AppSettingsPreferences.clearAll(context)
        }
    }

}