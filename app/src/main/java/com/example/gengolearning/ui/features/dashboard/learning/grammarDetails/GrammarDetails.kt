package com.example.gengolearning.ui.features.dashboard.learning.grammarDetails


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gengolearning.model.utils.AnalyticsHelper
import com.example.gengolearning.ui.components.ErrorModal
import com.example.gengolearning.ui.components.GeminAiBottomSheet
import com.example.gengolearning.ui.components.GrammarCardsAlertDialog
import com.example.gengolearning.ui.components.MyAppButton
import com.example.gengolearning.ui.features.dashboard.home.quiz.ErrorModal
import com.example.gengolearning.ui.features.navigation.Route
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.LightBlue
import com.example.gengolearning.ui.theme.Red
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrammarDetails(
    navController: NavController,
    grammarId: String,
    viewModel: GrammarDetailsViewModel = hiltViewModel()) {

    val selectedLanguage by viewModel.currentLanguage.collectAsState()
    val currentLanguage = selectedLanguage.code
    val grammarList by viewModel.grammar.collectAsState()
    val grammar = grammarList.find {it.id== grammarId} ?: return

    val exampleSentences = grammar.examples ?: emptyList()

    val exampleRows = remember {mutableStateListOf<String>()}

    var isEditMode by remember { mutableStateOf(false) }

    var grammarInput by remember { mutableStateOf(grammar.grammar) }
    var explanationInput by remember { mutableStateOf(grammar.explanation) }
    var dialogState by remember { mutableStateOf(false) }
    var geminAiDialog by remember{ mutableStateOf(false) }
    val dialogWidth = 400
    val dialogHeight = 500
    var generatedText by remember{ mutableStateOf<String?>(null)}
    val scope = rememberCoroutineScope()
    val chatGPTState by viewModel.chatGPTState.collectAsState()
    val grammarLanguage = stringResource(selectedLanguage.name)


    val editGrammarState by viewModel.editGrammarState.collectAsState()

    val errorModalSheetState = rememberModalBottomSheetState()
    var showErrorModal by remember {  mutableStateOf(false) }


    LaunchedEffect(chatGPTState) {
         when {
             chatGPTState is ChatGPTState.Error -> {
                 geminAiDialog = false
                 showErrorModal = true}

         }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.grammar_details))
                },
                navigationIcon = {
                    IconButton({
                        navController.popBackStack()
                    }) {
                        Image(
                            painter = painterResource(R.drawable.arrow_back2),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(White),
                actions = {
                    Image(
                        painter = painterResource(android.R.drawable.ic_menu_edit),
                        contentDescription = null,
                        colorFilter = if (isEditMode) ColorFilter.tint(BgBlue) else null,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember{ MutableInteractionSource() },
                                onClick = {
                                    isEditMode = !isEditMode

                                }
                            )
                    )
                }

            )
        }
    ) { innerPadding->

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)){

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                colors= CardDefaults.cardColors(White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row {

                    Box(modifier = Modifier
                             .weight(1f)
                             .padding(10.dp)
                         ){
                             SelectionContainer {
                                 Text(
                                     text = grammar.grammar,
                                     fontSize = 30.sp,
                                     modifier = Modifier,
                                     fontWeight = FontWeight.Bold,
                                 )
                             }

                    }
                }

                    SelectionContainer {

                        Text(
                            text = grammar.explanation,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                colors= CardDefaults.cardColors(White),
                elevation = CardDefaults.cardElevation(2.dp)
            ){
                Column {
                        Text(
                            text = stringResource(R.string.example_senteces),
                            fontSize = 30.sp,
                            modifier = Modifier.padding(5.dp),
                            fontWeight = FontWeight.Bold
                        )


                    LazyColumn {
                        items(exampleSentences) { example ->

                           Row{

                                Text(
                                    text = example,
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .weight(1f)
                                )


                             if (isEditMode)  Image(
                                   painter = painterResource(android.R.drawable.ic_delete),
                                   contentDescription = null,
                                   modifier = Modifier
                                       .size(24.dp)
                                       .clickable {
                                          viewModel.onExampleDelete( currentLanguage,
                                              grammarId,
                                              exampleSentences,
                                              exampleSentences.indexOf(example))
                                       }
                               )
                           }


                            HorizontalDivider(thickness = 1.dp, color = LightBlue,
                                modifier = Modifier.padding(start = 10.dp, end = 10.dp))
                        }

                    }

                    exampleRows.forEachIndexed { index, textValue ->

                        Row() {
                        TextField(
                            value = textValue,
                            onValueChange = { newValue ->
                                exampleRows[index] = newValue
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(5.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = White,
                                unfocusedContainerColor = White,
                                focusedIndicatorColor = Blue,
                                unfocusedIndicatorColor = Blue
                            )
                        )
                            Column {

                                Image(
                                    painter = painterResource(android.R.drawable.ic_input_add),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .clickable {
                                            viewModel.addNewExample(currentLanguage, grammarId,exampleRows[index])
                                            exampleRows[index] = ""
                                            exampleRows.removeAt(index)
                                        }
                                )
                            Image(
                                painter = painterResource(android.R.drawable.ic_delete),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .clickable {
                                        exampleRows.removeAt(index)
                                        AnalyticsHelper.logEvent("remove_example_grammar_details")
                                    }
                            )

                            }

                    }

                    }



                    Text(
                        text = stringResource(R.string.add_sentences),
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable{
                                exampleRows.add("")
                                AnalyticsHelper.logEvent("add_example_grammarDetails")
                            },
                        color = Blue
                    )
                }



            }


            MyAppButton(
                onClick = {
                    geminAiDialog = true


                    scope.launch {
                        viewModel.geminAiGrammar(grammar.grammar, grammarLanguage )
                    }

                    AnalyticsHelper.logEvent("ask_grammar_AI_button")
                },
                text = stringResource(R.string.gemin_ai)
            )

        }

        if (dialogState){
            GrammarCardsAlertDialog(
                onConfirm = {
                    dialogState = false
                    viewModel.onRemove( currentLanguage, grammarId)
                    navController.navigate(Route.GrammarList) { popUpTo(Route.GrammarList) { inclusive = true } }
                    isEditMode = false
                },
                onDismiss = {
                    dialogState = false
                }
            )
        }
        if (geminAiDialog) {


            GeminAiBottomSheet(
                close = {
                    geminAiDialog = false
                },
                isLoading = chatGPTState is ChatGPTState.Loading,
                text = (chatGPTState as? ChatGPTState.Success)?.response ?: ""
            )
        }
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 40.dp),
            visible = isEditMode,
            enter = slideInHorizontally { fullWidth -> fullWidth },
            exit = slideOutHorizontally { fullWidth -> fullWidth }
        ) {
            Column(modifier = Modifier.padding(bottom = 12.dp)) {

                Button(
                    onClick = {
                        viewModel.onEditGrammarDialog()
                        isEditMode = false
                        AnalyticsHelper.logEvent("save_changes_grammar_details")

                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .height(55.dp)
                        .padding(end = 12.dp, bottom = 5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BgBlue,
                        contentColor = White
                    ),
                    elevation = ButtonDefaults.buttonElevation(5.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                   Text(text = stringResource(R.string.edit_grammar_button))
                }
                Button(
                    onClick = {
                        dialogState = true
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .height(50.dp)
                        .padding(end = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Red,
                        contentColor = White
                    ),
                    elevation = ButtonDefaults.buttonElevation(5.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = stringResource(R.string.delete_grammar))
                }

            }
        }

    }
    }

    if (editGrammarState.showDialog) {
        EditGrammar(
            onDismiss = { viewModel.dismissEditGrammarDialog() },
            state = editGrammarState,
            viewModel = viewModel,
            grammarid = grammarId,
            currentLanguage = selectedLanguage
        )
    }

    if(showErrorModal) {

        ErrorModal(
            sheetState = errorModalSheetState,
            onClick = {
                showErrorModal = false
            },
            text = stringResource(R.string.common_error_internet_description)
        )
    }

}