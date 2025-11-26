package com.example.gengolearning.uielements.uimodels

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gengolearning.app.R
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyListTeachingModal(onClick: () -> Unit, sheetState: SheetState) {
    ModalBottomSheet(
        onDismissRequest = {
            onClick()
        },
        containerColor = White,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp, bottom = 30.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(R.drawable.infoicon100dp),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 20.dp)
            )

            Text(
                text = stringResource(R.string.myList_teaching_title),
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 10.dp),
                fontWeight = FontWeight.Bold
            )

            Row(modifier = Modifier
                .padding(bottom = 20.dp)) {
            Image(
                painter = painterResource(R.drawable.selectall),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(30.dp)

            )
                Text(text =stringResource(R.string.myList_teaching_icon1),
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    fontSize = 20.sp)
            }

            Row {
                Image(
                    painter = painterResource(R.drawable.cardsicon),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(30.dp)
                )
                Text(text = stringResource(R.string.myList_teaching_icon2),
                    modifier = Modifier.align(Alignment.CenterVertically),
                    fontSize = 20.sp)
            }

        }

        MyAppButton(
            onClick = { onClick() },
            text = stringResource(R.string.myList_teaching_button),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue
            )
        )

    }
}

@Preview
@Composable
private fun Preview() {

}