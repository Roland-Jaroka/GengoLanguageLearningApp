package com.example.gengolearning.uielements.uimodels

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gengolearning.app.R
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingModal(onClick: () -> Unit, sheetState: SheetState) {
    ModalBottomSheet(
        onDismissRequest = {onClick()},
        containerColor = White,
        sheetState = sheetState
    ) {
        Column(modifier = Modifier
            .padding(bottom = 10.dp)) {
            Image(
                painter = painterResource(R.drawable.panda_thanks),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(150.dp)
            )
            Text(text = stringResource(R.string.onboarding_title),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally))
            Text(text = stringResource(R.string.onboarding),
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                fontSize = 20.sp,)

            MyAppButton(
                onClick = {
                    onClick()
                },
                text = stringResource(R.string.onboarding_button),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue
                )
            )
        }

        }


}
