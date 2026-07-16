package com.example.gengolearning.ui.features.dashboard.home.apiwords

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.White
import com.example.gengolearning.ui.components.MyAppButton
import com.gengolearning.app.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JishoTutorialModal(onClick: ()-> Unit) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()


    ModalBottomSheet(
        onDismissRequest = {
            onClick()
        },
        containerColor = MaterialTheme.colorScheme.background,
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
                text = stringResource(R.string.jisho_tutorial_modal_title),
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                modifier = Modifier
                    .padding(bottom = 10.dp)

            )
            Text(
                text = stringResource(R.string.jisho_tutorial_modal_body),
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(bottom = 10.dp,start = 20.dp, end = 20.dp)
            )

            MyAppButton(
                text = stringResource(R.string.jisho_tutorial_modal_button),
                onClick = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onClick()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
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