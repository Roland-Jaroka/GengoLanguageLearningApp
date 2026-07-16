package com.example.gengolearning.ui.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gengolearning.ui.components.global.PressableRow
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LongTapBottomModal(modifier: Modifier = Modifier,
                       onDismiss: () -> Unit = {},
                       onClick: () -> Unit = {},
                       onCopyWord: () -> Unit = {},
                       onCopyPronounciation: () -> Unit = {},
                       onCopyTranslation: () -> Unit  = {},
                       currentLanguage: String) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {

        Column(verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = modifier) {

            PressableRow(
                onClick = {
                    onClick()
                    onDismissAnimation(scope, sheetState, onDismiss)

                },
                image = painterResource(R.drawable.edit),
                text = stringResource(R.string.edit_word_title)
            )
            PressableRow(
                onClick = {onCopyWord()
                    onDismissAnimation(scope, sheetState, onDismiss)
                          },
                image = painterResource(R.drawable.copy_icon),
                text = "Copy Word"
            )

            if (currentLanguage == "jp" || currentLanguage == "cn") {

                PressableRow(
                    onClick = {
                        onCopyPronounciation()
                        onDismissAnimation(scope, sheetState, onDismiss)
                    },
                    image = painterResource(R.drawable.copy_icon),
                    text = "Copy pronounciation"
                )
            }

            PressableRow(
                onClick = {onCopyTranslation()
                    onDismissAnimation(scope, sheetState, onDismiss)},
                image = painterResource(R.drawable.copy_icon),
                text = "Copy Translation"
            )

        }
    }



}


@OptIn(ExperimentalMaterial3Api::class)
fun onDismissAnimation(scope: CoroutineScope,
                       sheetState: SheetState,
                       onDismiss: () -> Unit) {
    scope.launch {
        sheetState.hide()
    }.invokeOnCompletion {
        if (!sheetState.isVisible) {
            onDismiss()
        }
    }
}


@Preview
@Composable
private fun Preview() {
    LongTapBottomModal(currentLanguage = "jp")

}
