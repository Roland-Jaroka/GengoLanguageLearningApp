package com.example.gengolearning.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.White

@Composable
fun WordFilterChips(selected: Boolean, title: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    FilterChip(
        selected = selected,
        onClick = {
            onClick()
        },
        label ={ Text(text = title)},
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = BgBlue
        ),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = BgBlue,
            selectedLabelColor = White,
            selectedLeadingIconColor = White),
        leadingIcon = {
            if (selected) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = null,
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        },
    )
}

@Preview
@Composable
private fun Preview() {
    WordFilterChips(selected = false, title = "Translation", onClick = {})

}