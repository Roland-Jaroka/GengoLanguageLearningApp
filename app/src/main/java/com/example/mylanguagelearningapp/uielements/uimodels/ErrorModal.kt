package com.example.mylanguagelearningapp.uielements.uimodels

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
import com.example.mylanguagelearningapp.R
import com.example.mylanguagelearningapp.ui.theme.BgBlue
import com.example.mylanguagelearningapp.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorModal(sheetState: SheetState, onClick: () -> Unit, text: String) {

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
            Text(text= "Oops",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold)
            Text(text = text,
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