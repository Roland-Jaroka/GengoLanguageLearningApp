package com.example.gengolearning.ui.components.global

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gengolearning.app.R

@Composable
fun SearchBar(
              modifier: Modifier = Modifier,
              searchInput: String,
              onValueChange: (String) -> Unit,
              onClear: () -> Unit = {},
              hasPlaceHolder: Boolean = false,
              hasLabel: Boolean = false,



) {
    OutlinedTextField(
        value = searchInput,
        onValueChange = {
            onValueChange(it)
        },
        label = {
            if(hasLabel) {
                Text(
                    stringResource(R.string.search),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.search),
                contentDescription = null
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = searchInput.isNotBlank()
            ) {
                IconButton(
                    onClick = {
                      onClear()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null
                    )
                }

            }
        },
        placeholder = {
            if (hasPlaceHolder) {
                Text(
                    text = stringResource(R.string.search)
                )
            }

        },

        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)
            .then(modifier),
        shape = RoundedCornerShape(20.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            cursorColor = MaterialTheme.colorScheme.secondary
        )
    )
}

@Preview
@Composable
private fun Preview() {
    SearchBar(
       searchInput =  "text",
        onValueChange = {},
        hasLabel = false,
        hasPlaceHolder = true

    )
}