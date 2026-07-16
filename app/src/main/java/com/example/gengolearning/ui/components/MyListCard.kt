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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R

@Composable
fun MyListCard(modifier: Modifier = Modifier,
                onClick: () -> Unit = {},
               title: String,
               buttonText: String) {
    Card(colors = CardDefaults.cardColors(
        containerColor = White
    ),
        elevation = CardDefaults.cardElevation(5.dp),
        modifier = modifier
            .width(170.dp)
            .height(180.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                ){
            Image(
                painter = painterResource(R.drawable.mylist_book),
                contentDescription = null,
                modifier = Modifier
                    .padding(10.dp)
                    .size(50.dp)
                ,
                alignment = Alignment.TopStart
            )
            Text(text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(start = 5.dp)
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    containerColor = MaterialTheme.colorScheme.primary
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

                Image(
                    painter = painterResource(R.drawable.list_icon),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 5.dp)
                )

                Text(
                    text = buttonText
                )
            }


        }
    }
}