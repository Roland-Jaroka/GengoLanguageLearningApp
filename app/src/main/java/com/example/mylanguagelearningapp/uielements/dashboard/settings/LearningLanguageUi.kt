package com.example.mylanguagelearningapp.uielements.dashboard.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylanguagelearningapp.R
import com.example.mylanguagelearningapp.model.CountryFlags
import com.example.mylanguagelearningapp.ui.theme.Blue
import com.example.mylanguagelearningapp.ui.theme.LightBlue
import com.example.mylanguagelearningapp.ui.theme.White

@Composable
fun LearningLanguageUi() {
    val flags= CountryFlags.entries

    Box(modifier = Modifier.fillMaxSize()){
        Card(modifier = Modifier
            .align(Alignment.Center)
            .width(300.dp)
            .padding(12.dp),
            colors = CardDefaults.cardColors(LightBlue),
            border = BorderStroke(2.dp, Blue)) {


            Text(
                text = "Select the language flag you want to learn",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding( 20.dp),
                fontSize = 20.sp
            )

            flags.forEach { flags ->
            Row(modifier = Modifier
                .align(Alignment.CenterHorizontally)) {
                Image(
                    painter = painterResource(flags.resID),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
                Text(
                    text = flags.name,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                RadioButton(
                    selected = false,
                    onClick = {  },
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = White,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )


            }

        }
    }

}

@Preview
@Composable
private fun Preview() {
    LearningLanguageUi()
}