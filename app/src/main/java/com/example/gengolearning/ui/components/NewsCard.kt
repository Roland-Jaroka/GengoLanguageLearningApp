package com.example.gengolearning.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.gengolearning.ui.theme.PandaBlack
import com.example.gengolearning.ui.theme.TransparentWhite
import com.example.gengolearning.ui.theme.White

@Composable
fun NewsCard(imageUrl: String? = null,
             title: String? = null,
             message: String? = null,
             clickable: Boolean = false,
             onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .height(150.dp)
            .padding(start = 10.dp, end = 10.dp)
            .clickable(enabled = clickable){
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        elevation = CardDefaults.cardElevation(5.dp)

    ) {
        Box {

        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                color = TransparentWhite
            ) {
                Column(
                    modifier = Modifier
                        .padding(5.dp)
                ) {
                    Text(
                        text = title ?: "This is the title",
                        fontSize = 20.sp,
                        color = PandaBlack
                    )

                    Text(
                        text = message ?: "This is the message",
                        modifier = Modifier
                            .padding(top = 5.dp),
                        fontSize = 12.sp,
                        color = PandaBlack

                    )
                }

            }



        }

    }
}

@Preview
@Composable
private fun Preview() {
    NewsCard()
}