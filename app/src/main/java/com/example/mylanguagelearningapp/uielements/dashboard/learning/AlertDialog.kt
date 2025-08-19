package com.example.mylanguagelearningapp.uielements.dashboard.learning


import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import com.example.mylanguagelearningapp.ui.theme.BgBlue
import com.example.mylanguagelearningapp.ui.theme.Red

@Composable
fun GrammarCardsAlertDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Grammar Card") },
        text = { Text("Are you sure you want to delete this grammar card?") },
        confirmButton = {

            Button(onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(Red)) {

                Text("Delete")
            }
        },
        dismissButton = {

            Button(onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(BgBlue)) {
                Text("Cancel")
            }
        }

    )

}