package com.example.gengolearning.ui.features.dashboard.home.aiquiz.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gengolearning.ui.theme.Blue
import com.gengolearning.app.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ArrowButton(onClick: () -> Unit = {},
                left: Boolean = false,
                modifier: Modifier = Modifier) {
    Button(
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            Blue
        ),
        modifier = modifier
            .padding(start = 5.dp, end = 5.dp),
        elevation = ButtonDefaults.buttonElevation(
            5.dp
        )
    ) {
        Image(
            painter = if (left) painterResource(R.drawable.outline_arrow_back) else painterResource(id = R.drawable.outline_arrow_forward),
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun Preview() {
    ArrowButton()
}