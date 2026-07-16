package com.example.gengolearning.ui.features.dashboard.home.addwords

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.model.appmodels.Words
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.components.TextButton
import com.example.gengolearning.ui.components.WordCard
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R


@Composable
fun WordInLibraryAlertDialog(
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
    currentLanguage: String,
    word1: Words,
    word2: Words
    ) {

    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        icon = {
            Column {
                Image(
                    painter = painterResource(R.drawable.duplicate_lighter),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterHorizontally)
                )

            }


        },
        title = {
            Text(
                text = stringResource(R.string.wordInLibrary_title),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(text = stringResource(R.string.wordInLibrary_description),
                    fontSize = 18.sp)

                WordCard(
                    word = word1.word,
                    translation = word1.translation,
                    pronunciation = word1.pronunciation,
                    currentLanguage = currentLanguage,
                    expandable = false
                )

                WordCard(
                    word = word2.word,
                    translation = word2.translation,
                    pronunciation = word2.pronunciation,
                    currentLanguage = currentLanguage,
                    expandable = false
                )
            }
        },
        confirmButton = {
            MyAppButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                },
                text = stringResource(R.string.wordInLibrary_confrim_Button),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            )
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                },
                text = stringResource(R.string.wordInLibrary_back)
            )
        },
        containerColor = MaterialTheme.colorScheme.background

    )
}

@Preview
@Composable
private fun Preview() {
//    WordInLibraryAlertDialog()
}