package com.example.gengolearning.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gengolearning.ui.features.navigation.Route
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.Inter
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R

@Composable
fun onBoardongRoot(navController: NavController) {
    onBoarding(
        onButtonClick = {
            navController.navigate(Route.Home)
        }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun onBoarding(
    onButtonClick: () -> Unit = {}
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {}){
        Scaffold(
            containerColor = White,
            modifier = Modifier
                .fillMaxSize(),
            bottomBar = {
                MyAppButton(
                    onClick = {
                        onButtonClick()
                    },
                    text = stringResource(R.string.onboarding_button),
                    modifier = Modifier
                        .navigationBarsPadding(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue
                    )
                )
            }
        ) {paddingValues ->
            Column(modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(R.drawable.onboardingpicture),
                    contentDescription = null
                )

                Text(
                    text = stringResource(R.string.onboarding_title),
                    modifier = Modifier
                        .padding(10.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(R.string.onboarding),
                    modifier = Modifier
                        .padding(10.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    fontFamily = Inter,
                    fontWeight = FontWeight.Light
                )
            }
        }


    }
}

@Preview
@Composable
private fun Preview() {
    onBoarding()
}