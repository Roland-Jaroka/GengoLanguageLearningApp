package com.example.gengolearning.ui.features.dashboard.home.quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.model.appmodels.QuizModes
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.components.TextButton
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizSelectorModal(onClick: (QuizModes) -> Unit = {},
                      currentLanguage: String,
                      onBack: () -> Unit,
                      onShuffle: () -> Unit = {}) {

    val selectedQuizMode = remember { mutableStateOf<QuizModes?>(null) }
    val selectableModes: List<QuizModes> =

        if (currentLanguage != "jp" && currentLanguage != "cn") {
            listOf(QuizModes.TranslationQuiz,
            QuizModes.WordQuiz,
                QuizModes.CardPlay    )
        } else {
            QuizModes.entries
        }
    var shuffledQuizMode by remember { mutableStateOf(false) }


    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            MyAppButton(
                onClick = {

                    if (selectedQuizMode.value != null && !shuffledQuizMode ) {
                        onClick(selectedQuizMode.value!!)
                    }
                    else {
                        onShuffle()
                    }
                },
                text = stringResource(R.string.quiz_selector_modal_button),
                colors = ButtonDefaults.buttonColors(Blue),
                enabled = selectedQuizMode.value != null || shuffledQuizMode
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
            selectableModes.forEach { quizModes ->

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
                            text = stringResource( quizModes.displayName),
                            fontSize = 20.sp,
                            modifier = Modifier
                                .weight(1f)
                        )

                        Checkbox(
                            checked = selectedQuizMode.value == quizModes,
                            onCheckedChange = {
                                selectedQuizMode.value = quizModes
                                shuffledQuizMode = false
                            },
                            modifier = Modifier
                        )
                    }
                }

            }

                //shuffledQuizMode

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
                            text = stringResource(R.string.shuffled_quiz_mode),
                            fontSize = 20.sp,
                            modifier = Modifier
                                .weight(1f)
                        )

                        Checkbox(
                            checked = shuffledQuizMode,
                            onCheckedChange = {
                                    shuffledQuizMode = true
                                selectedQuizMode.value = null
                            }
                        )
                    }
                }
        }
        }
    )

}