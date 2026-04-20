package com.example.gengolearning.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsBottomSheetModal(
    onDismiss: () -> Unit = {},
    onClick: () -> Unit = {}
) {

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        containerColor = White
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.infoicon100dp),
                contentDescription = null,
            )

            Text(
                text = "Syncronise your words ",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
            )

            Text(
                text = "By clicking here you can sync your app with the cloud",
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

            MyAppButton(
                onClick = {
                    onClick()
                },
                text = stringResource(R.string.okay_button),
                colors = ButtonDefaults.buttonColors(Blue),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 18.dp, end = 18.dp, top = 10.dp)
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    NewsBottomSheetModal()
}