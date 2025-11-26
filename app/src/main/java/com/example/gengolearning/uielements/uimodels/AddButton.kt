package com.example.gengolearning.uielements.uimodels

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import com.gengolearning.app.R

@Composable
fun AddButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val targetScale = if (isPressed || isHovered) 0.8f else 1.0f
    val scale by animateFloatAsState(targetScale, label = "")


    IconButton(
        onClick = onClick,
        modifier= modifier
            .scale(scale)
            .then(modifier),
        interactionSource = interactionSource
    ) {
        Image(
            painter = painterResource(R.drawable.plus),
            contentDescription = null,
        )
    }
}