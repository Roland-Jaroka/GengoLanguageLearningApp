package com.example.gengolearning.uielements.uimodels

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gengolearning.app.R
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoModal(sheetState: SheetState, onClick: () -> Unit) {

    ModalBottomSheet(
        onDismissRequest =  {
            onClick()
        },
        sheetState = sheetState,
        containerColor = White
    ) {
        Column {
            Image(
                painter = painterResource(R.drawable.alert_icon),
                contentDescription = null,
                modifier= Modifier
                    .size(90.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(text= "Still in development",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold)
            Text(text = "We're sorry for the inconvinience but the function you're trying to use is still in development",
                fontSize = 18.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                modifier = Modifier.padding(start = 30.dp, end = 30.dp, top = 10.dp))
            MyAppButton(
                onClick = {
                    onClick()
                },
                text = "Alright",
                colors = ButtonDefaults.buttonColors(BgBlue),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 18.dp, end = 18.dp, top = 10.dp)
            )
        }
    }

}