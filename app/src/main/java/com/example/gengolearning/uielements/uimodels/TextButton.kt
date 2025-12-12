package com.example.gengolearning.uielements.uimodels


import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.Transparent
import com.example.gengolearning.ui.theme.TransparentBlue

@Composable
fun TextButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Button(
        onClick = {
            onClick()
        },
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
           backgroundColor = if (isPressed) TransparentBlue else Transparent,
            contentColor = Blue
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .height(50.dp)
    ) {
        Text(text = text)
    }
}