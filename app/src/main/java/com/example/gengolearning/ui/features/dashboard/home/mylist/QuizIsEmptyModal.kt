package com.example.gengolearning.ui.features.dashboard.home.mylist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizIsEmptyModal(
    onDismiss: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        containerColor = White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.empty_box),
                contentDescription = null,
                modifier = Modifier
                    .size(45.dp)

            )

            Text(
                text = stringResource(R.string.empty_quiz_modal_title),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 5.dp)
            )

            Text(
                text = stringResource(R.string.empty_quiz_modal_description),
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 5.dp)
            )

            MyAppButton(
                onClick = {
                    onClick()
                    onDismiss()
                },
                text = stringResource(R.string.empty_quiz_modal_button),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue
                ),
                modifier = Modifier
                    .navigationBarsPadding()
            )



        }
    }
}

@Preview
@Composable
private fun Preview() {
    QuizIsEmptyModal()
}