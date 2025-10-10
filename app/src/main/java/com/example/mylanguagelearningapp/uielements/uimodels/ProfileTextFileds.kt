package com.example.mylanguagelearningapp.uielements.uimodels

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileTextFields(modifier: Modifier = Modifier, rowModifier: Modifier= Modifier, boldText: String, normalText: String) {
    Row(modifier = Modifier
        .padding(bottom = 10.dp)
        .then(rowModifier)
        ) {
        Text(
            text = boldText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = normalText,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(start = 10.dp)
                .then(modifier)
        )
    }

}