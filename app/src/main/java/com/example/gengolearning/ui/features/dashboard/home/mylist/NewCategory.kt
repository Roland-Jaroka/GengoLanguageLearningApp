package com.example.gengolearning.ui.features.dashboard.home.mylist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.White

@Composable
fun NewCategoryMaker(onDismiss: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .pointerInput(Unit){}
    ){
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = null,
            tint = BgBlue,
            modifier = Modifier
                .padding(start = 15.dp, top = 50.dp)
                .align(Alignment.TopStart)
                .clickable{
                    onDismiss()
                }
        )
        Column( modifier = Modifier
            .align(Alignment.TopCenter)
            .padding()) {

            Spacer(modifier = Modifier.height(100.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                shape = RoundedCornerShape(20.dp)
            )
        }
        MyAppButton(
            onClick = {},
            text = "Make",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .imePadding()
        )

    }
}

@Preview
@Composable
private fun Preview() {
    NewCategoryMaker()
}