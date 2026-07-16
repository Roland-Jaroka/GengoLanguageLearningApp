package com.example.gengolearning.ui.components.global

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.PandaBlack
import com.gengolearning.app.R

@Composable
fun PressableRow(modifier: Modifier = Modifier,
                 onClick: () -> Unit,
                 image: Painter,
                 text: String) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            }

    ) {
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 8.dp)
                .size(40.dp),
            colorFilter = if (isPressed) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null
        )

        Text(
            text = text,
            color = if (isPressed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp),
            fontWeight = FontWeight.Bold
        )
    }
}