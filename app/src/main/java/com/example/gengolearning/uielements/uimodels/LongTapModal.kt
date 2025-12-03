package com.example.gengolearning.uielements.uimodels


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.PandaBlack
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LongTapBottomModal(modifier: Modifier = Modifier, onDismiss: () -> Unit, onClick: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = White
    ) {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(true, color = Blue),
                ) {
                    onClick()
                }

        ) {
            Image(
                painter = painterResource(R.drawable.edit),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(40.dp),
                colorFilter = if (isPressed) ColorFilter.tint(BgBlue) else null
            )

            Text(text = "Edit Word",
                color = PandaBlack,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(start = 10.dp, top = 10.dp),
                fontWeight = FontWeight.Bold
            )
        }

    }
}
