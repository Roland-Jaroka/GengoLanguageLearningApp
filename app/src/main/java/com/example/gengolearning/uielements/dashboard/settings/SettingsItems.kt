package com.example.gengolearning.uielements.dashboard.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.ui.theme.BgBlue

@Composable
fun SettingItems(icon:@Composable () -> Unit, title: String, arrow: @Composable () -> Unit, onClick: () -> Unit, divider: Boolean = true) {
    Row(modifier = Modifier.fillMaxWidth()
        .clickable{onClick()},
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon()

        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            text = title,
            modifier = Modifier.align(Alignment.CenterVertically),
            fontSize = 20.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold
        )
        arrow()
    }
    if (divider){
    HorizontalDivider(color = BgBlue, thickness = 2.dp)}

}