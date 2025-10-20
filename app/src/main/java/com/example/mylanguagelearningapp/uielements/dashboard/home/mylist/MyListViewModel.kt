package com.example.mylanguagelearningapp.uielements.dashboard.home.mylist

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mylanguagelearningapp.model.QuizManager.quizzes
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import com.example.mylanguagelearningapp.model.Words
import com.example.mylanguagelearningapp.words.ChineseWords
import com.example.mylanguagelearningapp.words.JapaneseWords
import com.example.mylanguagelearningapp.words.JapaneseWords.auth
import com.example.mylanguagelearningapp.words.LanguageWords
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

class MyListViewModel: ViewModel() {

    val currentLanguage= UserSettingsRepository.language.value
    val repository= LanguageWords
    var words = repository.words



    var searchInput by mutableStateOf("")
        private set

    var labelInput by mutableStateOf("")
        private set

    val db= FirebaseFirestore.getInstance()
    val uid= FirebaseAuth.getInstance().currentUser?.uid.toString()

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

    fun toggleSelection(id: String) {
        if (selectedWords.contains(id)) {
            selectedWords.remove(id)
        } else {
            selectedWords.add(id)
        }
    }



    fun onRemove(){
        selectedWords.forEach { id->
            repository.onRemove(id, currentLanguage)
        }

        selectedWords.clear()
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



}