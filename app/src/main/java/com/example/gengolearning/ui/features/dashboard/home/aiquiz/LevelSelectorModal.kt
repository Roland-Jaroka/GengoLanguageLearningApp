package com.example.gengolearning.ui.features.dashboard.home.aiquiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.model.appmodels.Language
import com.example.gengolearning.model.appmodels.LanguageLevels
import com.example.gengolearning.model.appmodels.Languages
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.components.TextButton
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R

@Composable
fun LevelSelectorModal(currentLanguage: Language,
                       onClick: (String) -> Unit = {},
                       onBack: () -> Unit = {}) {

    val selectedLevel = remember { mutableStateOf("") }

    val selectableModes = when (currentLanguage.code) {
        "jp" -> LanguageLevels.JapaneseLevels.entries
        "cn" -> LanguageLevels.ChineseLevels.entries
        else -> LanguageLevels.GlobalLevels.entries
    }
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            MyAppButton(
                onClick = {
                    onClick(selectedLevel.value)
                },
                text = stringResource(R.string.quiz_selector_modal_button),
                colors = ButtonDefaults.buttonColors(Blue),
                enabled = selectedLevel.value != ""
            )
        },
        dismissButton = {
            TextButton(
                onClick = {
                      onBack()
                },
                text = stringResource(R.string.back_button)
            )
        },
        containerColor = White,
        title = {
            Text(
                stringResource(R.string.quiz_selector_modal_title)
            )
        },
        text = {
            Column {
                selectableModes.forEach { level ->

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = White,
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(start = 5.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = level.name,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .weight(1f)
                            )

                            Checkbox(
                                checked = selectedLevel.value == level.name,
                                onCheckedChange = {
                                    selectedLevel.value = level.name
                                },
                            )
                        }
                    }

                }
            }
        }
    )

}

@Preview
@Composable
private fun Preview() {
    LevelSelectorModal(
        currentLanguage = Languages.languagesList[0]
    )
}