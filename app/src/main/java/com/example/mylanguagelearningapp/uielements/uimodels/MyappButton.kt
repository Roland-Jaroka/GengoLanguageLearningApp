package com.example.mylanguagelearningapp.uielements.uimodels

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.example.mylanguagelearningapp.ui.theme.White

@Composable
fun MyAppButton(modifier: Modifier = Modifier,
                onClick: () -> Unit,
                text: String,
                enabled: Boolean = true,
                colors: ButtonColors = ButtonDefaults.buttonColors(),
                isLoading: Boolean = false) {
    val interactionSource= remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val targetScale = if (isPressed || isHovered) 0.9f else 1f
    val scale by animateFloatAsState(targetScale, label = "")
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .height(50.dp)
            .scale(scale),
        elevation = ButtonDefaults.buttonElevation(
            hoveredElevation = 10.dp,
            pressedElevation = 10.dp,
            defaultElevation = 5.dp
        ),
        shape = RoundedCornerShape(20.dp),
        colors = colors,
        interactionSource = interactionSource
    ) {

        if(!isLoading){
            Text(text= text,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp)}
        else {
            CircularProgressIndicator(
                color = White
            )
        }
    }

}

@Preview
@Composable
private fun ButtonPrev() {
    MyAppButton(modifier = Modifier, onClick = {}, text = "Preview Button")

}