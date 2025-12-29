package com.example.gengolearning.ui.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.example.gengolearning.ui.theme.PandaBlack
import com.example.gengolearning.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardHeader(
    titleText: String,
    userName: String,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {

     TopAppBar(
         title = {
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
                     color = PandaBlack ,
                     fontSize = 25.sp,
                     fontWeight = FontWeight.Bold,
                     modifier = Modifier.padding(top = 5.dp)
                 )
             }
         },
         colors = TopAppBarDefaults.topAppBarColors(
             containerColor = White,
             scrolledContainerColor = White
         ),
         scrollBehavior = scrollBehavior
     )


}