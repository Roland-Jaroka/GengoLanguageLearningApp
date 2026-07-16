package com.example.gengolearning.ui.features.dashboard.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.ui.theme.AmberPrimary
import com.example.gengolearning.ui.theme.AppColorTheme
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.CoralPrimary
import com.example.gengolearning.ui.theme.TealPrimary
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppThemeModal(
    onSelect: (AppColorTheme) -> Unit,
    currentTheme: AppColorTheme,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        AppThemeModalContent(
            onSelect = {
                onSelect(it)
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    onDismiss()
                }
                       },
            currentTheme
        )
    }
}

@Composable
private fun AppThemeModalContent(
    onSelect: (AppColorTheme) -> Unit = {},
    currentTheme: AppColorTheme
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp)
    ) {
        AppColorTheme.entries.forEach { theme ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shadowElevation = 2.dp,
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(25.dp)
            ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelect(theme)
                            }
                            .padding(start = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(
                                    when(theme) {
                                        AppColorTheme.BASIC -> BgBlue
                                        AppColorTheme.SUNSET -> CoralPrimary
                                        AppColorTheme.MIDNIGHT_TEAL -> TealPrimary
                                        AppColorTheme.Autumn -> AmberPrimary
                                    }
                                )
                        ) {}

                        Text(
                            text = when(theme){
                                AppColorTheme.BASIC -> stringResource(R.string.basic_theme)
                                AppColorTheme.SUNSET -> stringResource(R.string.sunset_theme)
                                AppColorTheme.MIDNIGHT_TEAL -> stringResource(R.string.midnight_teal_theme)
                                AppColorTheme.Autumn -> stringResource(R.string.autumn_theme)
                            },
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .weight(1f),
                            fontSize = 20.sp
                        )

                        Checkbox(
                            checked = if (theme == currentTheme) true else false,
                            onCheckedChange = {
                                onSelect(theme)
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.secondary
                            )
                        )
                    }



            }
        }
    }

}

@Preview
@Composable
private fun Preview() {
     AppThemeModalContent(
         onSelect = {},
         currentTheme = AppColorTheme.SUNSET
     )
}