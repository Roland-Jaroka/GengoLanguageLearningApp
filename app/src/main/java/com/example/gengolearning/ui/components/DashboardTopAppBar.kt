package com.example.gengolearning.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.example.gengolearning.ui.theme.PandaBlack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardHeader(
    titleText: String,
    userName: String,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    image: Painter,
    onClick: () -> Unit = {},
    isLoading: Boolean = false
) {

     TopAppBar(
         title = {
            if (!isLoading) {
                Row() {
                    Image(
                        painter = image,
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                onClick()
                            }

                    )
                    Column(
                        modifier = Modifier.padding(start = 10.dp)
                    ) {
                        Text(
                            text = "$titleText,",
                            color = PandaBlack,
                            fontSize = 20.sp
                        )
                        Text(
                            text = userName,
                            color = PandaBlack,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 5.dp)
                        )
                    }
                }
            }
             else Row() {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .shimmerEffect()
                )
                Column(
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(20.dp)
                            .shimmerEffect()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                            .height(25.dp)
                            .padding(top = 5.dp)
                            .shimmerEffect()
                    )
                }
            }
         },
         colors = TopAppBarDefaults.topAppBarColors(
             containerColor = MaterialTheme.colorScheme.background,
             scrolledContainerColor = MaterialTheme.colorScheme.background
         ),
         scrollBehavior = scrollBehavior
     )


}