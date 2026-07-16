package com.example.gengolearning.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gengolearning.app.R
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.White
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorModal(sheetState: SheetState,
               onClick: () -> Unit,
               text: String) {

            val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest =  {
            onClick()
        },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column {
            Image(
                painter = painterResource(R.drawable.alert_icon),
                contentDescription = null,
                modifier= Modifier
                    .size(90.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(text= stringResource(R.string.oops),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold)
            Text(text = text,
                fontSize = 18.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(start = 30.dp, end = 30.dp, top = 10.dp))
            MyAppButton(
                onClick = {

                    scope.launch {
                        sheetState.hide()

                    }.invokeOnCompletion {

                        if (!sheetState.isVisible) {
                            onClick()
                        }
                    }
                },
                text = stringResource(R.string.okay_button),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 18.dp, end = 18.dp, top = 10.dp)
            )
        }
    }

}