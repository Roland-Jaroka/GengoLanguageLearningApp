package com.example.gengolearning.ui.features.dashboard.home.mylist

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.gengolearning.app.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MyListFloatingActionButton(
    onAddClick: () -> Unit = {},
    onNewCategoryClick: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    val firstColor = MaterialTheme.colorScheme.primary
    val secondColor = MaterialTheme.colorScheme.secondary

    FloatingActionButtonMenu(
        expanded = expanded,
        button = {
            ToggleFloatingActionButton(
                checked = expanded,
                onCheckedChange = {expanded = it},
                containerColor = {
                    if (expanded) firstColor
                else secondColor}
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        if (expanded) R.drawable.close_button else R.drawable.add_button),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        },

    ){
        FloatingActionButtonMenuItem(
            onClick = {
                onAddClick()
            },
            text = {
                Text(text = stringResource(R.string.mylist_add_new_words_button),
                    color = MaterialTheme.colorScheme.onSecondary)
            },
            icon = {
                Icon(
                ImageVector.vectorResource(R.drawable.add_button),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            },
            containerColor = MaterialTheme.colorScheme.secondary
        )

        FloatingActionButtonMenuItem(
            onClick = {
                onNewCategoryClick()
            },
            text = {
                Text(text = stringResource(R.string.mylist_add_new_category_button),
                    color = MaterialTheme.colorScheme.onSecondary)
            },
            icon = {
                Icon(
                ImageVector.vectorResource(R.drawable.category_button),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            },
            containerColor = MaterialTheme.colorScheme.secondary
        )
    }


}