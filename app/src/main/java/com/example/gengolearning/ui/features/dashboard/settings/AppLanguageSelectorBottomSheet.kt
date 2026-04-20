package com.example.gengolearning.ui.features.dashboard.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import com.example.gengolearning.model.appmodels.AppLanguages
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppLanguageSelectorBottomSheet(modifier: Modifier = Modifier,
                                   onDismiss: () -> Unit = {}) {
      var selectedLanguage by remember { mutableStateOf(
          AppCompatDelegate.getApplicationLocales()[0]?.toLanguageTag() ?: "en"
      ) }
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        modifier = modifier,
        containerColor = White
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 20.dp)
        ) {
            AppLanguages.languages.forEach { language ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, bottom = 1.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = language.languageTag == selectedLanguage,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .scale(1.4f),
                        onClick = {
                            AppCompatDelegate.setApplicationLocales(
                                LocaleListCompat.forLanguageTags(language.languageTag)
                            )
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = BgBlue,
                            unselectedColor = Blue
                        )
                    )

                    Text(
                        text = stringResource(language.displayName),
                        fontSize = 25.sp
                    )

                }
            }
        }
    }

}

@Preview
@Composable
private fun Preview() {
    AppLanguageSelectorBottomSheet()
}