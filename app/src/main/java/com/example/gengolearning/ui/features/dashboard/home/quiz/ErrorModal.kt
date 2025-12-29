package com.example.gengolearning.ui.features.dashboard.home.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.White
import com.example.gengolearning.ui.components.MyAppButton
import com.gengolearning.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorModal(onClick : () -> Unit) {

    ModalBottomSheet(
        onDismissRequest = {
            onClick()
        },
        modifier = Modifier,
        containerColor = White
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.alert_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .padding(bottom = 10.dp)
            )

            Text(
                text = "Oops",
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                modifier = Modifier
                    .padding(bottom = 10.dp)

            )
            Text(
                text = "It seems like you have no internet connection",
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(bottom = 10.dp,start = 20.dp, end = 20.dp)
            )

            MyAppButton(
                text = "Okay",
                onClick = {
                    onClick()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = BgBlue
                )
            )
        }

    }

}

@Preview
@Composable
private fun Preview() {
    ErrorModal(
        onClick = {}
    )
}