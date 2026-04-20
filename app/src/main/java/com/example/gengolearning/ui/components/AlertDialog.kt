package com.example.gengolearning.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gengolearning.ui.theme.Red
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R

@Composable
fun GrammarCardsAlertDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = White,
        icon = {
            Image(
                painter = painterResource(R.drawable.delete),
                contentDescription = null,
                modifier = Modifier
                    .size(58.dp)
            )
        },
        title = { Text(stringResource(R.string.delete_grammar_dialog_title),
                     fontWeight = FontWeight.Bold) },
        text = { Text(stringResource(R.string.delete_grammar_dialog_description)) },
        confirmButton = {

            MyAppButton(onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(Red),
                text = stringResource(R.string.delete_grammar_dialog_confrimation_button) )



        },
        dismissButton = {

            TextButton(onClick = onDismiss,
                text =stringResource(R.string.delete_grammar_dialog_cancle_button) )


        }

    )

}

@Preview
@Composable
private fun Perview() {
    GrammarCardsAlertDialog(
        onConfirm = {},
        onDismiss = {}
    )
}