package com.example.gengolearning.uielements.uimodels



import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gengolearning.app.R
import com.example.gengolearning.ui.theme.BgBlue


@Composable
fun AppSettingsAlertDialog(modifier: Modifier = Modifier,
                           onConfirmation: () -> Unit,
                           onDismissRequest: () -> Unit) {
    AlertDialog(
        confirmButton = {
            MyAppButton(
                onClick = {
                    onConfirmation()
                },
                text = stringResource(R.string.app_setting_alert_dialog_button1),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BgBlue
                )
            )
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()

                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = BgBlue
                )
            ){
                Text(text = stringResource(R.string.app_setting_alert_dialog_button2))
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        modifier = modifier,
        icon = {
            Icon(
                painter = painterResource(R.drawable.alert_icon),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
            )
        },
        title = { Text(text = stringResource(R.string.app_setting_alert_dialog_title)) },
        text = {
            Text(stringResource(R.string.app_settings_alert_dialog_desc))
        },
    )

}