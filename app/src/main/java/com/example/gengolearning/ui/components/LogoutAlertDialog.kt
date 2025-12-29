package com.example.gengolearning.ui.components




import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.ui.theme.Red
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R

@Composable
fun LogoutDialog(onDismiss: () -> Unit,
                 onConfirm: () -> Unit,
                 title: String,
                 body: String,
                 confirmButtonText: String,
                 dismissButtonText: String ) {

    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        icon = {
            Image(
                painter = painterResource(R.drawable.logout_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(55.dp)
            )
        },
       title = {
           Text(
               text = title,
               fontWeight = FontWeight.Bold
           )
       },
        text = {
            Text(
                text = body,
                fontSize = 18.sp
            )
        },
        confirmButton = {
            MyAppButton(
                onClick = {
                    onConfirm()
                },
                text = confirmButtonText,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red
                )
            )

        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                },
                text = dismissButtonText
            )

        },
        containerColor = White

    )


}