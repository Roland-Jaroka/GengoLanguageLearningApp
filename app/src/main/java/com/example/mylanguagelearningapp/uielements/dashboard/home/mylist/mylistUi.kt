package com.example.mylanguagelearningapp.uielements.dashboard.home.mylist

//noinspection SuspiciousImport
import android.R
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Text
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import com.example.mylanguagelearningapp.ui.theme.BgBlue
import com.example.mylanguagelearningapp.ui.theme.Red
import com.example.mylanguagelearningapp.ui.theme.White
import com.example.mylanguagelearningapp.uielements.uimodels.MyAppButton
import com.example.mylanguagelearningapp.uielements.uimodels.WordCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyListUi(viewModel: MyListViewModel = viewModel(),
             navController: NavController
) {

    val wordList by viewModel.words.collectAsState()
    val searchInput = viewModel.searchInput
    val words = remember(wordList,searchInput){
        if (searchInput.isBlank()) wordList
        else wordList.filter{ word->
            listOf(word.word, word.pronunciation, word.translation).any{
                it.contains(searchInput, ignoreCase = true)
            }
        }
    }
    val labels by viewModel.labels.collectAsState(emptyList())
    val labelInput = viewModel.labelInput
    val onEdit = viewModel.onEdit
    var visible by remember { mutableStateOf(false) }
    val sheetState= rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val currentLanguage by UserSettingsRepository.language.collectAsState()

   var isFocused by remember { mutableStateOf(false) }
   val focusRequester = remember{ FocusRequester() }

    val textFieldWeight by animateFloatAsState(
        targetValue = if (isFocused) 1f else 0.5f
    )

    LaunchedEffect(Unit) {
        visible= true
        println("MyAppLabels: ${viewModel.labels}")
    }


Box(modifier = Modifier.fillMaxSize())
{
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Spacer(modifier = Modifier.height(30.dp))

        Box(modifier = Modifier.fillMaxWidth()) {

            Image(painter = painterResource(com.example.mylanguagelearningapp.R.drawable.arrow_back2),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 20.dp)
                    .clickable {
                        navController.navigate("home") { popUpTo("home") { inclusive = true } }
                    })

            Text(
            text = "My List",
            fontSize = 30.sp,
            color = BgBlue,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .align(Alignment.Center)
        )

        }

        Row {

            OutlinedTextField(
                value = searchInput,
                onValueChange = {
                    viewModel.onInputChanged(it)
                },
                label = {
                    Text(
                        "Search",
                        color = BgBlue
                    )
                },
                modifier = Modifier
                    .weight(textFieldWeight)
                    .focusRequester(focusRequester)
                    .onFocusChanged{ isFocused = it.isFocused},
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_menu_search),
                        contentDescription = null
                    )
                },

            )

            Image(
                painter = painterResource(R.drawable.ic_menu_edit),
                contentDescription = null,
                colorFilter = if (onEdit) ColorFilter.tint(BgBlue) else null,
                modifier = Modifier
                    .padding(start = 5.dp, top = 15.dp)
                    .clickable(
                        onClick = {
                            viewModel.onEdit = !viewModel.onEdit
                        }
                    )
            )
        }


        LazyColumn(modifier = Modifier.fillMaxSize()
            .animateContentSize()) {
            val maxAnimatedItems = 10
            itemsIndexed(words) { index, word ->
                val shouldAnimate = index < maxAnimatedItems
                AnimatedVisibility(
                    visible = visible,
                    enter = if (shouldAnimate) slideInHorizontally (initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(delayMillis = index * 100, durationMillis = 400))
                    else EnterTransition.None,

                ) {
                    WordCard(
                        word.word,
                        word.pronunciation,
                        word.translation,
                        isSelectable = onEdit,
                        isSelected = viewModel.selectedWords.contains(word.id),
                        onClick = {
                            viewModel.toggleSelection(word.id)
                            println("Selected words ID: ${word.id}")
                            println("Button clicked for word: ${word.word}")
                        },
                        currentLanguage)
                }
            }
            item {
                Spacer(modifier = Modifier.height(100.dp))

            }

        }

    }


    AnimatedVisibility(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(bottom = 40.dp),
        visible = onEdit,
        enter = slideInHorizontally { fullWidth -> fullWidth },
        exit = slideOutHorizontally { fullWidth -> fullWidth }
    ) {
        Column(modifier = Modifier.padding(bottom = 12.dp)) {

    Button(
        onClick = { viewModel.onRemove() },
        modifier = Modifier
            .align(Alignment.End)
            .height(50.dp)
            .padding(start = 12.dp, end = 12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Red,
            contentColor = White
        ),
        elevation = ButtonDefaults.buttonElevation(5.dp),
    ) { Text(text = "Remove Word")
    }
            Button(
                onClick = {
                    //isSheetOpen = true
                    Toast.makeText(context, "This function is still in development", Toast.LENGTH_SHORT).show()
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
            ) { Text(text = "Add label to words")
            }


                Button(
                    onClick = {
                        viewModel.onSendWordsToQuiz()
                        navController.navigate("quiz")
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
                    Text(text = "Send words to Quiz")
                }

            if (currentLanguage== "jp" || currentLanguage== "cn") {

                Button(
                    onClick = {
                        viewModel.onSendWordsToDrawingQuiz()
                        navController.navigate("drawing")
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
                    Text(text = "Send words to Drawing Quiz")
                }
            }



        }

    }

}
    if(isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = {
                isSheetOpen = false
            },
            sheetState = sheetState,
            containerColor = White
        ) {
            Column(modifier = Modifier){
                OutlinedTextField(
                    value = labelInput,
                    onValueChange = {
                        viewModel.onLabelInputChanged(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp))

                LazyRow(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp)) {
                    items(labels.size) { label ->

                        Chip(
                            onClick = {
                                labels.elementAt(label)?.let { viewModel.labeling(it) }
                                viewModel.onLabelInputChanged("")
                                isSheetOpen = false
                            },
                            label = {
                                labels.elementAt(label)?.let { Text(text = it) }
                            },
                            modifier = Modifier.padding(end = 10.dp)
                        )
                    }
                }

                MyAppButton(
                    text = "Add",
                    onClick = {
                        viewModel.labeling(labelInput)
                        viewModel.onLabelInputChanged("")
                        isSheetOpen = false
                    },
                    colors = ButtonDefaults.buttonColors(BgBlue)
                )
            }

        }
    }
}
