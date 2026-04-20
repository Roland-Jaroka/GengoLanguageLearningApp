package com.example.gengolearning.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gengolearning.model.appmodels.DashboardFeatures
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.White

@Composable
fun ButtonCards(modifier: Modifier = Modifier,
                navController: NavController,
                features: DashboardFeatures,
                flag: Int,
                language: String,
                onClick: () -> Unit = {}) {
    Card(colors = CardDefaults.cardColors(
        containerColor = White
    ),
        elevation = CardDefaults.cardElevation(5.dp),
        modifier = modifier
            .height(180.dp),
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()) {
            Image(
                painter = painterResource(features.icon),
                contentDescription = null,
                modifier = Modifier
                    .padding(10.dp)
                    .size(50.dp),
                alignment = Alignment.TopStart
            )
            Text(
                text = stringResource(features.title),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(start = 5.dp)
            )

            if (features.id != "LanguageChange") {

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = White,
                        containerColor = BgBlue
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        hoveredElevation = 10.dp,
                        pressedElevation = 10.dp,
                        defaultElevation = 5.dp
                    ),
                    shape = RoundedCornerShape(20.dp),
                    onClick = {
                        onClick()
                    },

                    ) {
                    Icon(
                        painter = painterResource(features.buttonIcon),
                        contentDescription = null,
                        modifier = if (features.buttonSize != null) Modifier.size(features.buttonSize) else Modifier,
                        tint = White
                    )

                    Text(
                        text = stringResource(features.buttonText),
                        modifier = Modifier
                            .padding(start = 2.dp)
                    )
                }
        } else {
                   LanguageSelectionDashboardRow(
                       flag,
                       language = language,
                       onClick = {
                           navController.navigate(features.route)
                       }
                   )
        }



        }
    }
}