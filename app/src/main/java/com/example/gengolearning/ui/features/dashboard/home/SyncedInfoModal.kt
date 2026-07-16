package com.example.gengolearning.ui.features.dashboard.home


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.theme.AppColorTheme
import com.example.gengolearning.ui.theme.MyLanguageLearningAppTheme
import com.gengolearning.app.R
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncedInfoModal(
    onDismiss:() -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Content(
          onClick = {
               scope.launch {
                   sheetState.hide()
               }.invokeOnCompletion {
                   onDismiss()
               }
          }
        )

    }
}

@Composable
private fun Content(onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp )
    ) {
        Icon(
            painter = painterResource(R.drawable.wifi_icon_2),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .size(70.dp)
        )

        Text(
            text = stringResource(R.string.synced_modal_title),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Text(
            text = stringResource(R.string.synced_modal_text),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 5.dp, end = 5.dp)
        )

        MyAppButton(
            onClick = {
                onClick()
            },
            text = stringResource(R.string.common_common_modal_button_text),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                MaterialTheme.colorScheme.secondary
            )
        )

    }
}

@Preview
@Composable
private fun Preview() {
    MyLanguageLearningAppTheme(appColorTheme = AppColorTheme.SUNSET) {
        Content()
    }
}