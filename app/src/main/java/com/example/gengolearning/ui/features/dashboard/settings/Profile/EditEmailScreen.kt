package com.example.gengolearning.ui.features.dashboard.settings.Profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R

@Composable
fun EditProfileNameScreen(close: () -> Unit = {},
                    save: () -> Unit = {},
                    onEmailChange: (String) -> Unit = {},
                    name: String = "",
                    isLoading: Boolean = false,
                          state: ProfileNameEditState) {

       Box(
           modifier = Modifier
               .fillMaxSize()
               .background(White)
               .pointerInput(Unit) {}
       ) {
           IconButton(
               onClick = {
                   close()
               },
               modifier = Modifier
                   .align(Alignment.TopStart)
                   .padding(top = 50.dp),
               colors = IconButtonDefaults.iconButtonColors(
                   contentColor = MaterialTheme.colorScheme.primary
               )
           ) {
               Icon(
                   imageVector = Icons.Default.Close,
                   contentDescription = null
               )
           }

           OutlinedTextField(
               value = name,
               onValueChange = {
                   onEmailChange(it.take(10))
               },
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(start = 10.dp, end = 10.dp, top = 100.dp)
                   .align(Alignment.TopCenter),
               shape = RoundedCornerShape(20.dp),
               label = {
                   Text(text = stringResource(R.string.profile_name_hint))
               },
               maxLines = 1,
               isError = state.fieldError,
               supportingText = {
                   if (state.fieldError) {
                       state.fieldValidationMessage?.let { messageRes->
                           Text(text = stringResource(id = messageRes))
                       }
                   }
               }

           )


           MyAppButton(
               onClick = {
                   save()
               },
               text = stringResource(R.string.edit_button),
               modifier = Modifier
                   .align(Alignment.BottomCenter)
                   .navigationBarsPadding(),
               isLoading = isLoading,
               colors = ButtonDefaults.buttonColors(
                   containerColor = MaterialTheme.colorScheme.secondary
               )
           )
       }


}

