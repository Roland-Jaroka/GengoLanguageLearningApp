package com.example.gengolearning.ui.features.dashboard.home.mylist.editcategory

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.gengolearning.model.appmodels.WordCategories
import com.example.gengolearning.ui.components.CategoryListCard
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.features.dashboard.home.mylist.makeNewCategory.ErrorTypes
import com.example.gengolearning.ui.theme.AppColorTheme
import com.example.gengolearning.ui.theme.MyLanguageLearningAppTheme
import com.gengolearning.app.R
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCategoryRoot(viewModel: EditCategoryViewModel = hiltViewModel(),
                 navController: NavController,
                 categoryId: String?
) {



    val state by viewModel.editCategoryUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { events ->
            when (events) {
                is EditCategoryEvents.Navigate -> navController.popBackStack()
            }
        }
    }

EditCategory(
    state,
    onAction = viewModel::onAction,
    onNavigateBack = {
        navController.popBackStack()
    }
)
}
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
   private fun EditCategory(
        state: EditCategoryUiState,
        onAction: (EditCategoryActions)-> Unit = {},
        onNavigateBack: () -> Unit = {}
    ) {

        val colorPicker = rememberColorPickerController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onNavigateBack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = { Text(
                    text = stringResource(R.string.edit_category_title)
                )
                },
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            MyAppButton(
                onClick = {
                    onAction(EditCategoryActions.OnEdit(state.category))
                },
                text = stringResource(R.string.edit_category_button),
                modifier = Modifier
                    .padding(10.dp)
                    .navigationBarsPadding(),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)

            )
        }
    ) {
        paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.edit_category_description),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 10.dp, bottom = 10.dp)
                )
                OutlinedTextField(
                    value = state.category,
                    onValueChange = {
                        onAction(EditCategoryActions.OnNameChange(it.take(10)))
                    },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    maxLines = 1,
                    isError = state.error != null,
                    supportingText = {
                        when (state.error) {
                            is ErrorTypes.isBlank -> {
                                Text(text = stringResource(R.string.edit_category_blank_error))
                            }
                            is ErrorTypes.categoryIsExist -> {
                                Text(text = stringResource(R.string.edit_category_already_exist_error))
                            }
                            else -> {}
                        }
                    }
                )

                Text(
                    text = stringResource(R.string.edit_category_preview),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 10.dp, bottom = 10.dp)
                )

                CategoryListCard(
                    category = WordCategories(
                        categoryName = state.category,
                        color = state.color
                    ) ,
                    number = 0
                )
                Row(
                    modifier = Modifier
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text(
                        text = stringResource(R.string.edit_category_color),
                        fontSize = 20.sp,
                    )

                    IconButton(
                        onClick = {
                            onAction(EditCategoryActions.ShowColorPicker)
                        }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.colorwheel),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }

                    AnimatedVisibility(
                        visible = state.showColorPicker ,
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = stringResource(R.string.color_reset_title),
                                fontSize = 20.sp,
                            )

                            IconButton(
                                onClick = {
                                    colorPicker.selectCenter(false)
                                }
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.reset_button),
                                    contentDescription = null
                                )
                            }
                        }

                    }


                }

                AnimatedVisibility(
                    visible = state.showColorPicker,
                )  {
                    HsvColorPicker(
                        modifier = Modifier
                            .height(300.dp)
                            .padding(20.dp),
                        controller = colorPicker,
                        onColorChanged = { envelope ->
                            onAction(EditCategoryActions.OnColorChange(envelope.color))
                        },
                        initialColor = Color(state.color)

                    )
                }

            }


        }
    }
}

@Preview
@Composable
private fun Preview() {
    MyLanguageLearningAppTheme(appColorTheme = AppColorTheme.SUNSET) {
        EditCategory(
            state = EditCategoryUiState()
        )
    }
}