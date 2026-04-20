package com.example.gengolearning.model.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@SuppressLint("ContextCastToActivity")
@Composable
fun ImeModeAdjustNothing() {
    val activity = LocalContext.current as Activity
    DisposableEffect(Unit) {
        val originalMode = activity.window.attributes.softInputMode

        activity.window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
        )

        onDispose {
            activity.window.setSoftInputMode(originalMode)
        }
    }
}