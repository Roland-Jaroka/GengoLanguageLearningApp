package com.example.gengolearning.ui.features.dashboard.home.mylist

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.model.AppSettingsPreferences
import com.example.gengolearning.model.utils.QuizManager.quizzes
import com.example.gengolearning.data.repositories.UserSettingsRepository
import com.example.gengolearning.model.appmodels.Words
import com.example.gengolearning.data.repositories.LanguageWords
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MyListViewModel @Inject constructor(
    private val repository: LanguageWords,
     userSettingsRepository: UserSettingsRepository
): ViewModel() {

    val currentLanguage= userSettingsRepository.language

    var words = repository.words.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )



    var searchInput by mutableStateOf("")
        private set

    var labelInput by mutableStateOf("")
        private set

    var longTappedWord by mutableStateOf<Words?>(null)
        private set


    val labels = repository.words.map {
        list-> list.mapNotNull { it.label }.toSet()
    }

    fun onInputChanged(newInput: String) {
        searchInput = newInput
    }

    fun onLabelInputChanged(newInput: String){
        labelInput= newInput
    }

    var onEdit by mutableStateOf(false)

    val selectedWords = mutableStateListOf<String>()



    fun tutorialModal(context: Context) = AppSettingsPreferences.showMyListTutorial(context)

    fun toggleSelection(id: String) {
        if (selectedWords.contains(id)) {
            selectedWords.remove(id)
        } else {
            selectedWords.add(id)
        }
    }

    fun selectAll(){

        if (selectedWords.size != words.value.size) {
            selectedWords.clear()
            selectedWords.addAll(words.value.map { it.id })
        } else {
            selectedWords.clear()
        }
    }



    fun onRemove(){
        viewModelScope.launch {
            selectedWords.forEach { id ->
                repository.onRemove(id, currentLanguage.value)
            }

            selectedWords.clear()
        }
    }

    fun onSendWordsToDrawingQuiz(){
        val words= words.value
        quizzes.clear()
        selectedWords.forEach { id ->
            words.find { it.id == id }?.let { quizzes.add(it)
                println("Word added to drawing quiz: $quizzes")
            }
        }

    }

    fun onSendWordsToQuiz(){
        val words= words.value
        println("Words: $words")
        quizzes.clear()
        selectedWords.forEach { id->
            words.find{it.id==id}?.let{word->
                quizzes.add(word)
                println("Word added to quiz: $quizzes")

            }
        }
    }

    fun labeling(label: String){
        val auth = FirebaseAuth.getInstance()
        val uid= auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return
        if (label.isBlank()) return

        selectedWords.forEach { id ->

            Firebase.firestore
                .collection("users")
                .document(uid)
                .collection("words")
                .document(id)
                .update("label", label)
                .addOnSuccessListener {
                    println("Word labeled successfully")
                }
        }


    }

    fun onHomepage(){

        if (selectedWords.isEmpty()) return


          viewModelScope.launch {

              val allWords = words.value
              allWords.filter { it.id in selectedWords && it.isOnHomePage == false || it.isOnHomePage == null }
                  .forEach { words ->
                      repository.onHomePage(words.id,  true)
                      println("Word added to home page: $words")
                  }
              allWords.filter { it.id !in selectedWords && it.isOnHomePage == true }
                  .forEach { words ->
                      repository.onHomePage(words.id,  false)
                      println("Word removed from home page: $words")
                  }
              selectedWords.clear()
          }

    }

    fun onHomeCard(){
        selectedWords.clear()
        val homePageWords = words.value.filter {it.isOnHomePage == true}
            if(homePageWords.isNotEmpty()){
                homePageWords.forEach {
                    selectedWords.add(it.id)
                }
            }
            //At first when no word is selected this will select all the words cause all of them is
            //visible on the home screen
            else {
               selectedWords.addAll(words.value.map { it.id })
            }


    }

    fun setMyListTutorial(context: Context){
        viewModelScope.launch {
            AppSettingsPreferences.setMyListTutorialShown(context, false)
        }
    }

    fun onLongTap(word: Words) {
        longTappedWord = word
    }

    fun setTutorial(context: Context) {
        viewModelScope.launch {
            AppSettingsPreferences.setMyListTutorialShown(context, false)
        }
    }




}