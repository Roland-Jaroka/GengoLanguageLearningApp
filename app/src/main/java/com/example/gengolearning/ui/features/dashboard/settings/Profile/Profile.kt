package com.example.gengolearning.ui.features.dashboard.settings.Profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.gengolearning.model.appmodels.Languages
import com.example.gengolearning.ui.components.MyTopAppBar
import com.example.gengolearning.ui.components.ProfileTextFields
import com.example.gengolearning.ui.components.TextButton
import com.example.gengolearning.ui.utils.SuccessScreen
import com.example.gengolearning.ui.theme.Blue
import com.example.gengolearning.ui.theme.White
import com.example.gengolearning.ui.utils.UnsuccessfullScreen
import com.gengolearning.app.R
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileMenu(navController: NavController, viewModel: ProfileViewModel = hiltViewModel()){
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val state by viewModel.profileState.collectAsState()
    val editEmailState by viewModel.profileNameEditState.collectAsState()
    val email= currentUser?.email.toString()
    val currentLanguage by viewModel.currentLanguage.collectAsState(
        Languages.languagesList[0]
    )
    val wordList by viewModel.wordsList.collectAsState()
    val grammarList by viewModel.grammar.collectAsState()
    val appversion = LocalContext.current.packageManager.getPackageInfo(LocalContext.current.packageName, 0).versionName.toString()
    val image by viewModel.image.collectAsState()
    var editProfileName by remember {   mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.loadImageFromGallery(it) }

    }

    Box() {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                MyTopAppBar(
                    modifier = Modifier,
                    title = "Profile",
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Box(modifier = Modifier.align(Alignment.TopCenter)) {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, top = 100.dp),
                        elevation = CardDefaults.cardElevation(10.dp),
                        colors = CardDefaults.cardColors(White)
                    ) {
                        //Elements:
                        Column(modifier = Modifier.padding(top = 50.dp, start = 50.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                ProfileTextFields(
                                    boldText = stringResource(R.string.profile_menu_profile_name),
                                    normalText = state.profileName
                                )

                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .padding(bottom = 10.dp)
                                        .clickable {
                                            editProfileName = true
                                        },
                                    tint = LocalContentColor.current
                                )
                            }
                            ProfileTextFields(
                                boldText = stringResource(R.string.profile_menu_email),
                                normalText = email
                            )
                            ProfileTextFields(
                                boldText = stringResource(R.string.language),
                                normalText = stringResource(currentLanguage.name)
                            )

                            val currentLanguageName = stringResource(currentLanguage.name)

                            ProfileTextFields(
                                boldText = stringResource(
                                    R.string.profile_menu_words_row,
                                    currentLanguageName
                                ),
                                normalText = wordList.size.toString()
                            )

                            ProfileTextFields(
                                boldText = stringResource(R.string.profile_menu_grammar_points),
                                normalText = grammarList.size.toString()

                            )

                            ProfileTextFields(
                                boldText = stringResource(R.string.profile_menu_words_number),
                                normalText = state.wordCount.toString()
                            )

                            ProfileTextFields(
                                boldText = stringResource(R.string.profile_menu_languages),
                                normalText = state.languageCount.toString()
                            )

                            ProfileTextFields(
                                boldText = stringResource(R.string.profile_menu_app_version),
                                normalText = appversion
                            )
                        }


                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .background(White)
                            .clip(RoundedCornerShape(30.dp))
                            .border(2.dp, Blue, RoundedCornerShape(30.dp))
                            .clickable {
                                launcher.launch("image/*")
                            }

                    ) {
                        Image(
                            painter = when (val image = image) {
                                null -> painterResource(R.drawable.profile)
                                else -> {
                                    rememberAsyncImagePainter(image.image)
                                }
                            },
                            contentDescription = "profile",
                            modifier = Modifier.size(130.dp)
                        )

                    }
                }
                if (image != null) {
                    TextButton(
                        onClick = {
                            viewModel.deletePicture(image!!)
                        },
                        text = "Clear Profile picture",
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 20.dp)
                            .navigationBarsPadding()
                    )
                }
            }
        }
    }

    if (editProfileName) {
        when (editEmailState.success) {
            is EditState.Idle -> {
                EditProfileNameScreen(
                    close = {
                        editProfileName = false
                    },
                    onEmailChange = {
                        viewModel.onNameInputChange(it)
                    },
                    name = editEmailState.name,
                    save = { viewModel.changeProfileName(editEmailState.name) },
                    isLoading = editEmailState.isLoading,
                    state = editEmailState
                )
            }
            is EditState.Success -> {
                SuccessScreen(
                    onDismiss = {
                        editProfileName = false
                        viewModel.resetEmailEditState()
                    },
                    description = stringResource(R.string.profile_name_edit_success_description)
                )
            }
            is EditState.Failure -> {
                UnsuccessfullScreen(
                    onDismiss = {
                        editProfileName = false
                        viewModel.resetEmailEditState()
                    },
                    description = stringResource(R.string.something_went_wrong)
                )
            }
        }


        }

}