package com.example.gengolearning.uielements.uimodels

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gengolearning.app.R
import com.example.gengolearning.ui.theme.White

@ExperimentalMaterial3Api
@Composable
fun MyTopAppBar(modifier: Modifier,
                title: String,
                route: String,
                navController: NavController,
                scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(title)
        },
        navigationIcon = {
            IconButton({
                navController.popBackStack()
            }) {
                Image(
                    painter = painterResource(R.drawable.arrow_back2),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(White),
        scrollBehavior = scrollBehavior

    )

}