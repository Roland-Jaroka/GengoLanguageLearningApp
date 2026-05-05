package com.example.gengolearning.ui.features.dashboard.home.mylist

//noinspection SuspiciousImport

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import com.example.gengolearning.model.utils.AnalyticsHelper
import com.example.gengolearning.ui.components.CategoryDeleteAlertDialog
import com.example.gengolearning.ui.components.CategoryListCard
import com.example.gengolearning.ui.components.DeleteWordAlertDialog
import com.example.gengolearning.ui.components.InfoModal
import com.example.gengolearning.ui.components.LongTapBottomModal
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.components.MyListTeachingModal
import com.example.gengolearning.ui.components.MyTopAppBar
import com.example.gengolearning.ui.components.WordCard
import com.example.gengolearning.ui.components.global.SearchBar
import com.example.gengolearning.ui.features.navigation.Route
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.PandaBlack
import com.example.gengolearning.ui.theme.Red
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyListUi(viewModel: MyListViewModel = hiltViewModel(),
             navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState()

    val list by viewModel.words.collectAsState()

    var visible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val currentLanguage by viewModel.currentLanguage.collectAsState()


    remember{ FocusRequester() }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val tutorial by viewModel.tutorialModal(context).collectAsState(initial = false)






    LaunchedEffect(Unit) {
        visible= true
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event->
            when (event) {
                is MyListUiEvents.NavigateToAddWords ->
                    navController.navigate(Route.EditWord(state.longTappedWord?.id))
                is MyListUiEvents.NavigateToNewCategory ->
                    navController.navigate(Route.EditCategory(state.longTappedCategory?.id.toString()))
                is MyListUiEvents.NavigateToQuiz ->
                    navController.navigate(Route.Quiz)
            }
        }
    }

    Scaffold(
        topBar= { MyTopAppBar(
            modifier = Modifier,
            title = stringResource(R.string.my_list_button),
            onBackClick = {
                navController.popBackStack()
            },
            scrollBehavior = scrollBehavior,
            actions = {

                if (state.words.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            viewModel.myListActions(MyListActions.OnListViewChange)
                        }
                    ) {
                        if (!state.categoryListView) {
                            Image(
                                painter = painterResource(R.drawable.categories),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp)
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.cards),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp)
                            )
                        }
                    }
                }
            }
        ) },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !state.onEdit
            ) {

                MyListFloatingActionButton(
                    onAddClick = {
                        navController.navigate(Route.AddWords(
                            word = null,
                            pronunciation = null,
                            translation = null
                        ))
                    },
                    onNewCategoryClick = {
                        navController.navigate(Route.NewCategory)
                    }
                )
            }

        },
    ){ paddingValues ->
Box(modifier = Modifier
    .fillMaxSize()
    .padding(top = paddingValues.calculateTopPadding()))
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {


        Row {
            SearchBar(
                searchInput = state.searchInput,
                onValueChange = {
                   viewModel.myListActions(MyListActions.OnInputChanged(it))
                },
                onClear = {
                    viewModel.myListActions(MyListActions.OnInputChanged(""))
                },
                modifier = Modifier.weight(1f),
                hasLabel = true
            )
            AnimatedVisibility(
                visible = state.onEdit && !state.categoryListView,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Row {
                    IconButton(
                        onClick = {
                            viewModel.myListActions(MyListActions.OnHomeCard)

                        },
                    ) {
                        Image(
                            painter = painterResource(R.drawable.cardsicon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp),
                        )
                    }

                    IconButton(
                        onClick = {
                            viewModel.myListActions(MyListActions.OnSelectAll)
                        }

                        ) {
                        Image(
                            painter = painterResource(R.drawable.selectall),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp),
                            colorFilter = if (state.selectedWords.size == state.words.size) ColorFilter.tint(
                                BgBlue
                            ) else null
                        )
                    }
                }
            }

            Image(
                painter = painterResource(android.R.drawable.ic_menu_edit),
                contentDescription = null,
                colorFilter = if (state.onEdit) ColorFilter.tint(BgBlue) else null,
                modifier = Modifier
                    .padding(start = 5.dp, top = 15.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            viewModel.myListActions(MyListActions.OnEdit)
                        }
                    )
            )

        }


        if (state.words.isNotEmpty() ) {


            if (!state.categoryListView) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                        .animateContentSize()
                ) {
                    val maxAnimatedItems = 10
                    itemsIndexed(state.words) { index, word ->
                        val shouldAnimate = index < maxAnimatedItems
                        AnimatedVisibility(
                            visible = visible,
                            enter = if (shouldAnimate) slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(
                                    delayMillis = index * 100,
                                    durationMillis = 400
                                )
                            )
                            else EnterTransition.None,

                            ) {
                            val category = state.categories.filter { it.categoryName in word.category }
                            WordCard(
                                word.word,
                                word.pronunciation,
                                word.translation,
                                isSelectable = state.onEdit,
                                isSelected = state.selectedWords.contains(word.id),
                                onClick = {
                                    viewModel.myListActions(MyListActions.OnToggleSelection(word.id))
                                },
                                longTap = {
                                    viewModel.myListActions(MyListActions.OnLongTap(word))

                                },
                                currentLanguage = currentLanguage,
                                categories = category,
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(200.dp))

                    }

                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                        .animateContentSize()
                ) {
                    itemsIndexed(state.categories) { index, category ->
                        val wordInCategory =
                            state.words.filter { it.category.contains(category.categoryName) }
                        var showWords by remember { mutableStateOf(false) }
                        CategoryListCard(
                            category = category,
                            number = wordInCategory.size,
                            onClick = {
                                showWords = !showWords
                            },
                            onLongClick = {
                                viewModel.myListActions(MyListActions.OnCategoryLongTap(category))
                            },
                            showWords = showWords,
                            isEditMode = state.onEdit,
                            isSelected = state.selectedCategories.contains(category.categoryName),
                            onCheckedChange = {

                                viewModel.myListActions(MyListActions.OnToggleCategorySelection(category.categoryName, wordInCategory))

                            },
                            onCategoryDelete = {
                                viewModel.myListActions(MyListActions.OnCategoryDeleteButton(category))
                            }
                        )
                        AnimatedVisibility(
                            visible = showWords
                        ) {
                            Column {

                                wordInCategory.forEach { word ->

                                    WordCard(
                                        word = word.word,
                                        pronunciation = word.pronunciation,
                                        translation = word.translation,
                                        currentLanguage = word.language,
                                        isSelectable = state.onEdit,
                                        isSelected = state.selectedWords.contains(word.id)
                                    )
                                }
                            }
                        }

                    }
                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        }
        // Empty listview
        else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(R.drawable.emptybox),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(80.dp)
                )

                Text(
                    text = if (state.words.isEmpty() && list.isEmpty()) stringResource(R.string.empty_word_list) else stringResource(
                        R.string.wordlist_empty_search
                    ),
                    fontSize = 20.sp,
                    color = PandaBlack,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.CenterHorizontally)
                )

                MyAppButton(
                    onClick = {
                        navController.navigate(Route.AddWords(
                            word = null, translation = null, pronunciation = null
                        ))
                        AnalyticsHelper.logEvent("addwords_button_mylist")
                    },
                    text = stringResource(R.string.add_words_button),
                    colors = ButtonDefaults.buttonColors(Blue)

                )
            }

        }

    }



    AnimatedVisibility(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(bottom = 40.dp),
        visible = state.onEdit,
        enter = slideInHorizontally { fullWidth -> fullWidth },
        exit = slideOutHorizontally { fullWidth -> fullWidth }
    ) {
        Column(modifier = Modifier.padding(bottom = 12.dp)) {

            Button(
                onClick = {
                    viewModel.myListActions(MyListActions.OnDeleteCategoryButton)
                    AnalyticsHelper.logEvent("remove_button_mylist")
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .height(50.dp)
                    .padding(start = 12.dp, end = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red,
                    contentColor = White
                ),
                elevation = ButtonDefaults.buttonElevation(5.dp),
            ) {
                Icon(imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.remove_word_button))
            }


            Button(
                onClick = {
                    viewModel.myListActions(MyListActions.OnHomePage)
                    AnalyticsHelper.logEvent("homePage_button_mylist")
                    navController.navigate(Route.Home) {
                        popUpTo("home") { inclusive = true }
                    }

                },
                modifier = Modifier
                    .align(Alignment.End)
                    .height(50.dp)
                    .padding(start = 12.dp, end = 12.dp, top = 5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BgBlue,
                    contentColor = White
                ),
                elevation = ButtonDefaults.buttonElevation(5.dp)
            ) {
                Icon(imageVector = Icons.Filled.Image,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.homepage_button))
            }

            Button(
                onClick = {
                   viewModel.myListActions(MyListActions.OnAddCategoryButton)
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .height(50.dp)
                    .padding(start = 12.dp, end = 12.dp, top = 5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BgBlue,
                    contentColor = White
                ),
                elevation = ButtonDefaults.buttonElevation(5.dp)
            ) {
                Icon(imageVector = Icons.Filled.Category,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.add_category_button))
            }



            Button(
                onClick = {
                    viewModel.myListActions(MyListActions.OnSendWordsToQuiz)
                    AnalyticsHelper.logEvent("quiz_button_mylist")
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .height(50.dp)
                    .padding(start = 12.dp, end = 12.dp, top = 5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BgBlue,
                    contentColor = White
                ),
                elevation = ButtonDefaults.buttonElevation(5.dp)
            ) {
                Icon(imageVector = Icons.Filled.Quiz,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.send_words_to_quiz_button))
            }


            if (currentLanguage == "jp" || currentLanguage == "cn") {

                Button(
                    onClick = {
                        viewModel.myListActions(MyListActions.OnSendWordsToDrawingQuiz)
                        navController.navigate(Route.DrawingQuiz)
                        AnalyticsHelper.logEvent("drawing_quiz_button_mylist")
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .height(50.dp)
                        .padding(start = 12.dp, end = 12.dp, top = 5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BgBlue,
                        contentColor = White
                    ),
                    elevation = ButtonDefaults.buttonElevation(5.dp)
                ) {
                    Icon(imageVector = Icons.Filled.Draw,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.send_words_to_drawingquiz_button))
                }
            }


        }

    }



}


    }



    if (tutorial){
        MyListTeachingModal(
            sheetState = sheetState,
            onClick = { viewModel.setMyListTutorial(context)})
    }

    if (state.longTap) {
        LongTapBottomModal(
            onDismiss = { viewModel.myListActions(MyListActions.OnDismissWordLongTapModal) },
            onClick = {

               viewModel.myListActions(MyListActions.OnEditWord)

            }
        )
    }

    if (state.categoryLongTap) {
        CategoryLongTapModal(
            onDismiss = {
                viewModel.myListActions(MyListActions.OnDismissCategoryLongTapModal)
            },
            onClick = {
               viewModel.myListActions(MyListActions.OnEditCategory)
            }
        )
    }

    if (state.newCategoryModal) {
        InfoModal(
            sheetState = sheetState,
            onClick = {
                viewModel.myListActions(MyListActions.OnDismissNewCategoryModal)
            }
        )
    }

    if (state.showDeleteDialog) {
        DeleteWordAlertDialog(
            onConfirm = {
                viewModel.myListActions(MyListActions.OnRemove)
                        },
            onDismiss = { viewModel.myListActions(MyListActions.OnHideDeleteWordDialog) }
        )
    }

    if(state.showCategoryDeleteDialog) {
        CategoryDeleteAlertDialog(
            onConfirm = {
                viewModel.myListActions(MyListActions.OnDeleteCategory(state.categoryToDelete!!))

            },
            onDismiss = {
                viewModel.myListActions(MyListActions.OnDismissCategoryDeleteDialog)
            }
        )
    }

    if (state.showCategoryBottomSheet) {
        CategorySelectorBottomSheet(
            categories = state.categories,
            onDismiss = { viewModel.myListActions(MyListActions.OnDismissCategoryBottomSheet) },
            onClick = {
                viewModel.myListActions(MyListActions.OnAddCategoryToSelectedWords(it.categoryName))
            }
        )
    }

    if (state.quizIsEmptyModal) {
        QuizIsEmptyModal(
            sheetState = sheetState,
            onDismiss = {
                viewModel.myListActions(MyListActions.OnDismissQuizIsEmptyModal)
            }
        )
    }
}
