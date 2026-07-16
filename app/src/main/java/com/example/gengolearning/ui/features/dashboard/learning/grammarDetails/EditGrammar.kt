package com.example.gengolearning.ui.features.dashboard.learning.grammarDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gengolearning.model.appmodels.Language
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R

@Composable
fun EditGrammar(onDismiss: () -> Unit = {},
                state: EditGrammarState,
                viewModel: GrammarDetailsViewModel,
                grammarid: String?,
                currentLanguage: Language) {

    LaunchedEffect(Unit) {
        viewModel.onStart()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(Unit) {}
    ) {
        Column(modifier = Modifier
            .align(Alignment.TopStart)
            .offset(y= 50.dp)
            )
        {

        IconButton(
            onClick = {
                onDismiss()
            },

        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
            Text(
                text = stringResource(R.string.edit_grammar_grammar_point_title),
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(10.dp),
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = stringResource(R.string.edit_grammar_grammar_point_description),
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(start = 15.dp, top = 5.dp, bottom = 10.dp),
            )

            OutlinedTextField(
                value = state.title,
                onValueChange = {
                       viewModel.onEditGrammarTitleChange(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 20.dp),
                shape = RoundedCornerShape(20.dp),
                maxLines = 1,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.onSecondary,
                    focusedContainerColor = MaterialTheme.colorScheme.onSecondary),
                isError = state.titleFieldValidation,
                supportingText = {
                    if (state.titleFieldValidation) {
                            state.titleFieldValidationMessage?.let { message->
                                Text(text = stringResource(message))
                            }
                        }
                    }

            )

            Text(
                text = stringResource(R.string.edit_grammar_grammar_summary_title),
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(10.dp),
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = stringResource(R.string.edit_grammar_grammar_summary_description),
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(start = 15.dp, top = 5.dp, bottom = 10.dp),
            )

             OutlinedTextField(
                 value = state.summary,
                 onValueChange = {
                     viewModel.onEditGrammarSummaryChange(it)
                 },
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(10.dp),
                 shape = RoundedCornerShape(20.dp),
                 colors = TextFieldDefaults.colors(
                     focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                     unfocusedContainerColor = MaterialTheme.colorScheme.onSecondary,
                     focusedContainerColor = MaterialTheme.colorScheme.onSecondary,
                 ),
                 isError = state.summaryFieldValidation,
                 supportingText = {
                     if (state.summaryFieldValidation) {
                         state.summaryFieldValidationMessage?.let { message->
                             Text(text = stringResource(message))
                         }
                     }
                 }

             )
         }

        MyAppButton(
            onClick = {

              viewModel.onSave(grammarid = grammarid,
                    language = currentLanguage.code,
                    grammarTitle = state.title,
                    explanation = state.summary)


            },
            text = stringResource(R.string.save_changes),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        )
    }
}
