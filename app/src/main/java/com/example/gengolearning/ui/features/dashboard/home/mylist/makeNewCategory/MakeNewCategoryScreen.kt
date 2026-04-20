package com.example.gengolearning.ui.features.dashboard.home.mylist.makeNewCategory

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gengolearning.model.appmodels.WordCategories
import com.example.gengolearning.ui.components.CategoryListCard
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeNewCategoryScreen(
    viewModel: NewCategoryViewmodel = hiltViewModel(),
    navController: NavController,
) {

    val colorPicker = rememberColorPickerController()
    val state by viewModel.uiState.collectAsState()
    var showColorPicker by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
           viewModel.uiEvent.collect {event ->
               when (event) {
                   is UiEvent.CategoryCreated -> {
                       navController.popBackStack()
                   }
               }
           }
    }

    Scaffold(
        containerColor = White,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White
                ),
                title = {
                    Text(
                        text = stringResource(R.string.new_category_title)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.arrow_back2),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                }
            )
        },
        bottomBar = {
            MyAppButton(
                onClick = {
                    viewModel.onSaveCategory(
                        category = state.category,
                        color = state.color
                    )
                },
                text = stringResource(R.string.make_category_button),
                modifier = Modifier
                    .padding(10.dp)
                    .navigationBarsPadding(),
                colors = ButtonDefaults.buttonColors(Blue)
            )
        }

    ) {paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) {
                Text(
                    text =stringResource(R.string.new_category_description),
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(10.dp),

                )

                OutlinedTextField(
                    value = state.category,
                    onValueChange = {
                        viewModel.onInputChanged(it.take(10))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    shape = RoundedCornerShape(20.dp),
                    maxLines = 1,
                    label = {
                        Text(text = stringResource(R.string.edit_category_description))
                    },
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



                AnimatedVisibility (state.category.isNotBlank(),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.edit_category_preview),
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(start = 15.dp, top = 10.dp, bottom = 10.dp)
                        )

                        FilterChip(
                            selected = false,
                            onClick = {},
                            label = {
                                Text(text = state.category)
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = state.color
                            ),
                            modifier = Modifier
                                .padding(10.dp)
                                .align(Alignment.CenterHorizontally),
                            shape = RoundedCornerShape(20.dp),
                        )

                        CategoryListCard(
                            category = WordCategories(
                                categoryName = state.category,
                                color = state.color.toArgb()
                            ),
                            number = 0,
                        )

                        Row(
                            modifier = Modifier
                                .padding(15.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            Text(
                                text = stringResource(R.string.edit_category_color),
                                fontSize = 20.sp,
                            )

                            IconButton(
                                onClick = {
                                    showColorPicker = !showColorPicker
                                }
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.colorwheel),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(30.dp)
                                )
                            }

                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                            )

                            AnimatedVisibility(
                                visible = state.color != White ,
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
                            visible = showColorPicker,
                        ) {

                            HsvColorPicker(
                                modifier = Modifier
                                    .height(300.dp)
                                    .padding(20.dp),
                                controller = colorPicker,
                                onColorChanged = { envelope ->
                                    viewModel.onColorChanged(envelope.color)
                                }
                            )
                        }
                    }

                    }

            }


        }
    }
}