package com.example.gengolearning.ui.features.dashboard.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
fun JishoTutorialModal(onClick: ()-> Unit) {

    val sheetState = rememberModalBottomSheetState()


    ModalBottomSheet(
        onDismissRequest = {
            onClick()
        },
        containerColor = White,
        sheetState = sheetState
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(10.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.infoicon100dp),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(bottom = 10.dp)

            )
            Text(
                text = "Jisho dictionary",
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                modifier = Modifier
                    .padding(bottom = 10.dp)

            )
            Text(
                text = "This dictionary uses the Jisho.org API for search for Japanese words",
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
    JishoTutorialModal(
        onClick = {}
    )
}