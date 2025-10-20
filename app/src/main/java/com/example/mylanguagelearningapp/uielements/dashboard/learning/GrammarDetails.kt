package com.example.mylanguagelearningapp.uielements.dashboard.learning


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mylanguagelearningapp.grammar.ChineseGrammar
import com.example.mylanguagelearningapp.grammar.JapaneseGrammar
import com.example.mylanguagelearningapp.grammar.LanguageGrammar
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import com.example.mylanguagelearningapp.ui.theme.BgBlue
import com.example.mylanguagelearningapp.ui.theme.Blue
import com.example.mylanguagelearningapp.ui.theme.LightBlue
import com.example.mylanguagelearningapp.ui.theme.Red
import com.example.mylanguagelearningapp.ui.theme.White
import com.example.mylanguagelearningapp.uielements.uimodels.GrammarCardsAlertDialog
import kotlinx.coroutines.launch
import kotlin.String

@Composable
fun GrammarDetails(navController: NavController,
                   grammarId: String?,
                   viewModel: GrammarDetailsViewModel = viewModel()) {

    val currentLanguage by UserSettingsRepository.language.collectAsState()
    val grammarList by LanguageGrammar.grammar.collectAsState()
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

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(top = 16.dp)){

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            Box(modifier = Modifier.fillMaxWidth()
                .padding(16.dp)){

                Image(painter = painterResource(com.example.mylanguagelearningapp.R.drawable.arrow_back2),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 5.dp)
                        .align(Alignment.CenterStart)
                        .clickable {
                            navController.navigate("learning") {popUpTo("learning") { inclusive = true }}
                        })
                Text(
                    text = "Grammar",
                    fontSize = 30.sp,
                    color = Blue,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                colors= CardDefaults.cardColors(White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row {
                    if (isEditMode) {
                        TextField(
                            value = grammarInput,
                            onValueChange = { newText->
                                grammarInput = newText
                            },
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = White,
                                unfocusedContainerColor = White,
                                focusedIndicatorColor = Blue,
                                unfocusedIndicatorColor = Blue
                            )
                        )
                    } else {

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
                    Image(
                        painter = painterResource(android.R.drawable.ic_menu_edit),
                        contentDescription = null,
                        colorFilter = if (isEditMode) androidx.compose.ui.graphics.ColorFilter.tint(Blue) else null,
                        modifier = Modifier
                            .size(45.dp)
                            .padding(10.dp)
                            .clickable{
                            isEditMode = !isEditMode
                        })
                }

                if (isEditMode) {
                    TextField(
                        value = explanationInput,
                        onValueChange = { newText->
                            explanationInput = newText
                        },
                        modifier = Modifier
                            .width(320.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = White,
                            unfocusedContainerColor = White,
                            focusedIndicatorColor = Blue,
                            unfocusedIndicatorColor = Blue
                        )
                    )
                } else {

                    SelectionContainer {

                        Text(
                            text = grammar.explanation,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                Text(
                    text = "Grammar category",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(10.dp)
                )

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
                            text = "Example sentences",
                            fontSize = 30.sp,
                            modifier = Modifier.padding(5.dp),
                            fontWeight = FontWeight.Bold
                        )


                    LazyColumn {
                        items(exampleSentences) { example ->

                           Row(){
                            SelectionContainer {
                                Text(
                                    text = example,
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .weight(1f)
                                )
                            }

                             if (isEditMode)  Image(
                                   painter = painterResource(android.R.drawable.ic_delete),
                                   contentDescription = null,
                                   modifier = Modifier
                                       .padding(5.dp)
                                       .clickable {
                                          viewModel.onExampleDelete( currentLanguage, grammarId, exampleSentences, exampleSentences.indexOf(example))
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
                                            JapaneseGrammar.loadGrammar()
                                        }
                                )
                            Image(
                                painter = painterResource(android.R.drawable.ic_delete),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .clickable {
                                        exampleRows.removeAt(index)
                                    }
                            )

                            }

                    }

                    }



                    Text(
                        text = "Add sentences",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable{
                                exampleRows.add("")
                            },
                        color = Blue
                    )
                }



            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(start = 12.dp, end = 12.dp, top = 5.dp),

                onClick = {
                    geminAiDialog = true

                   scope.launch {
                      viewModel.geminAiGrammar(grammar.grammar)
                   }

                }
            ) {
                Text(text = "Get examples from GeminAi")
            }

        }
        AnimatedVisibility(
            visible = isEditMode,
            enter = slideInVertically { fullHeight -> fullHeight },
            exit = slideOutVertically { fullHeight -> fullHeight },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
        ) {
        Box(
            modifier = Modifier
                .height(150.dp)
                .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                .background(White),
            //TODO hide the scroll behind the button
        ){


                Column() {

                    Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        viewModel.onSave(grammarId, currentLanguage, grammarInput, explanationInput)
                        isEditMode = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(start = 12.dp, end = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BgBlue,
                        contentColor = White
                    )
                ) {
                    Text(text = "Save Changes")
                }

                    Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        dialogState = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(start = 12.dp, end = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Red,
                        contentColor = White
                    )
                ) {
                    Text(text = "Delete Grammar Card")
                }

            }
            }
        } //Box End

        if (dialogState){
            GrammarCardsAlertDialog(
                onConfirm = {
                    dialogState = false
                    viewModel.onRemove( currentLanguage, grammarId)
                    navController.navigate("learning") { popUpTo("learning") { inclusive = true } }
                    isEditMode = false
                },
                onDismiss = {
                    dialogState = false
                }
            )
        }
        if (geminAiDialog) {
            Dialog(
                onDismissRequest = { geminAiDialog = false}
            ) {
                GeminAiChatUi(
                onClick = { geminAiDialog = false },
                dialogWidth,
                dialogHeight,
                    state = chatGPTState
            )
            }
        }

    }

}