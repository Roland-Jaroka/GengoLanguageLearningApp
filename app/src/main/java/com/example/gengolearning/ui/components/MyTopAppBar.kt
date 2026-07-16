package com.example.gengolearning.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R

@ExperimentalMaterial3Api
@Composable
fun MyTopAppBar(modifier: Modifier,
                title: String,
                scrollBehavior: TopAppBarScrollBehavior? = null,
                actions: @Composable () -> Unit = {},
                onBackClick: () -> Unit = {},
                onBackAction: () -> Unit = {}
) {

    TopAppBar(
        modifier = modifier,
        title = {
            Text(title)
        },
        navigationIcon = {
            IconButton(onClick = dropUnlessResumed {
                onBackClick()
                onBackAction()
            }

            ) {
                Image(
                    painter = painterResource(R.drawable.arrow_back2),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.background),
        scrollBehavior = scrollBehavior,
        actions = {
            actions()
        }

    )

}