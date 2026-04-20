package com.example.gengolearning.ui.features.dashboard.home.mylist.editword


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gengolearning.model.appmodels.Languages
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.components.TextButton
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.Red
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R

@Composable
fun EditWord(
    navController: NavController,
    wordId: String?,
    viewModel: EditWordViewModel = hiltViewModel()
) {
    val word by viewModel.word.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState(
        Languages.languagesList[0]
    )


    val wordInputError = viewModel.wordInputError?.let { id -> stringResource(id)}
    val translationInputError = viewModel.translationInputError?.let { id -> stringResource(id) }

    val addedCategories by viewModel.addedCategories.collectAsState()
    val deleteableCategories by viewModel.deleteableCategories.collectAsState()




    LaunchedEffect(Unit) {
        viewModel.events.collect { event->
            when (event) {
                is EditWordEvents.Navigate -> navController.popBackStack()
            }
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())){
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.edit_word_title),
                fontSize = 30.sp,
                color = Blue
            )
            Text(
                text = stringResource(R.string.edit_word_description),
                modifier = Modifier
                    .padding(top = 10.dp)
            )

            OutlinedTextField(
                value = word?.word ?: "",
                onValueChange = {
                    viewModel.onWordInputChange(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp, top = 20.dp),
                shape = RoundedCornerShape(20.dp),
                singleLine = true,
                label = { Text(stringResource(R.string.word_button)) },
                isError = wordInputError != null,
                supportingText = {
                    if (wordInputError != null) {
                        Text(text = "*$wordInputError",
                            color = Red
                        )
                    }
                }
            )

            if (currentLanguage.code == "jp" || currentLanguage.code == "cn") {

                OutlinedTextField(
                    value = word?.pronunciation ?: "",
                    onValueChange = {
                        viewModel.onPronunciationInputChange(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp, top = 20.dp),
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true,
                    label = { Text(stringResource(R.string.pronuncitaon_button)) }
                )
            }

            OutlinedTextField(
                value = word?.translation ?: "",
                onValueChange = {
                    viewModel.onTranslationInputChange(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp, top = 20.dp),
                shape = RoundedCornerShape(20.dp),
                singleLine = true,
                label = { Text(stringResource(R.string.translation_button)) },
                isError = translationInputError != null,
                supportingText = {
                    if (translationInputError != null) {
                        Text(text = "*$translationInputError",
                            color = Red
                        )
                    }
                }
            )

            if (addedCategories.isNotEmpty() || deleteableCategories.isNotEmpty()) {
            Text(
                text = stringResource(R.string.edit_word_categories)
            )

            FlowRow {
                addedCategories.forEach { category ->
                    FilterChip(
                        selected = false,
                        onClick = {
                            viewModel.onCategoryClick(category)
                        },
                        label = {
                            Text(text = category.categoryName)
                        },
                        colors = if (category.color != null) FilterChipDefaults.filterChipColors(
                            Color(category.color)
                        ) else FilterChipDefaults.filterChipColors(
                            containerColor = Blue
                        ),
                        modifier = Modifier
                            .padding(5.dp),
                        shape = RoundedCornerShape(20.dp),
                        leadingIcon = {
                            Image(
                                painter = painterResource(R.drawable.plus),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(20.dp)
                            )
                        }

                        )
                }
            }

            Text(
                text = stringResource(R.string.edit_word_categories_delete)
            )

            FlowRow {
                deleteableCategories.forEach { category ->
                    FilterChip(
                        selected = false,
                        onClick = {
                            viewModel.onDeleteableCategoryClick(category)
                        },
                        label = {
                            Text(text = category.categoryName)
                        },
                        colors = if (category.color != null) FilterChipDefaults.filterChipColors(
                            Color(category.color)
                        ) else FilterChipDefaults.filterChipColors(
                            containerColor = Blue
                        ),
                        modifier = Modifier
                            .padding(5.dp),
                        shape = RoundedCornerShape(20.dp),
                        leadingIcon = {
                            Image(
                                painter = painterResource(R.drawable.delete_icon_mini),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(25.dp)
                            )
                        }

                        )
                }
            }
            }



            MyAppButton(
                onClick = {
                    viewModel.onUpdate(currentLanguage.code)
                },
                text = stringResource(R.string.edit_button),
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, top = 20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue,
                    contentColor = White
                )
            )

            TextButton(
                onClick = {
                    navController.popBackStack()
                },
                text = stringResource(R.string.edit_word_back_button)
            )

        }
    }
}


