package com.example.gengolearning.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White

@Composable
fun LanguageSelectionRow(flag: Int, language: String, selected: Boolean, onSelect: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 10.dp),
        shadowElevation = 2.dp,
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(25.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable{
                    onSelect()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(flag),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 20.dp)
                    .size(50.dp)

            )

            Text(
                text = language,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(1f),
                fontSize = 20.sp
            )

            Checkbox(
                checked = selected,
                onCheckedChange = {
                    onSelect()
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}
