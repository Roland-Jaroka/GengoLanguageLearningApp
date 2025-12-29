package com.example.gengolearning.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gengolearning.app.R
import com.example.gengolearning.ui.theme.Pink

@Composable
fun NewDesign(modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{}
            .then(modifier),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Pink
        )

    ) {
        Image(
            painter = painterResource(R.drawable.addnewwordcard),
            contentDescription = null,
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Add New Word",
            fontSize = 20.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 10.dp),
            lineHeight = 30.sp
        )
        Text(
            text = "Add new words to your dictionary",
            fontSize = 15.sp,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier
                .padding(start = 10.dp, bottom = 10.dp)
        )

    }
}

@Preview
@Composable
private fun Preview() {
    NewDesign()
}
