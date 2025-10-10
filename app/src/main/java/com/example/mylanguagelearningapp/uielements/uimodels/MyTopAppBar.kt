package com.example.mylanguagelearningapp.uielements.uimodels

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mylanguagelearningapp.R
import com.example.mylanguagelearningapp.ui.theme.White

@ExperimentalMaterial3Api
@Composable
fun MyTopAppBar(modifier: Modifier, title: String,  route: String,navController: NavController) {
    TopAppBar(
        title = {
            Text(title)
        },
        navigationIcon = {
            IconButton({
                navController.navigate(route){
                    popUpTo(route) { inclusive = true }
                }
            }) {
                Image(
                    painter = painterResource(R.drawable.arrow_back2),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(White)

    )

}