package com.example.mylanguagelearningapp.uielements.dashboard.home.mylist

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mylanguagelearningapp.model.QuizManager.quizzes
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import com.example.mylanguagelearningapp.words.ChineseWords
import com.example.mylanguagelearningapp.words.JapaneseWords
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyListViewModel: ViewModel() {

    val currentLanguage= UserSettingsRepository.language.value
    val words = when(currentLanguage) {
        "jp" -> JapaneseWords.wordList
        "cn"-> ChineseWords.chinseWordsList
        else -> JapaneseWords.wordList
    }

    var searchInput by mutableStateOf("")
        private set

    val db= FirebaseFirestore.getInstance()
    val uid= FirebaseAuth.getInstance().currentUser?.uid.toString()
    fun onInputChanged(newInput: String) {
        searchInput = newInput
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

    val filteredWords by derivedStateOf {
        if (searchInput.isBlank()) {
            words.sortedBy { it.word }
        } else {
            words.filter { word ->
                listOf(word.word, word.pronunciation, word.translation).any{
                    it.contains(searchInput, ignoreCase = true)}
            }.sortedBy { it.word }
        }
    }

    fun onRemove(){
        if (currentLanguage=="jp") {
            selectedWords.forEach { id ->
                db.collection("users")
                    .document(uid)
                    .collection("words")
                    .document(id)
                    .delete()
                    .addOnSuccessListener {
                        println("Word removed from database")
                    }
            }
            selectedWords.forEach{id->
                JapaneseWords.wordList.find { it.id == id }?.let { word ->
                    JapaneseWords.wordList.remove(word)
                }
            }
            selectedWords.clear()

        }

        else if (currentLanguage=="cn"){
            selectedWords.forEach{id->
                db.collection("users")
                    .document(uid)
                    .collection("chineseCollection")
                    .document("chinese")
                    .collection("chineseWords")
                    .document(id)
                    .delete()
                    .addOnSuccessListener {
                        println("Word removed from database")
                    }

            }

            selectedWords.forEach { id->
                ChineseWords.chinseWordsList.find { it.id == id }?.let { word->
                    ChineseWords.chinseWordsList.remove(word)
                }
            }
            selectedWords.clear()
        }

    }

    fun onSendWordsToDrawingQuiz(){
        quizzes.clear()
        selectedWords.forEach { id ->
            words.find { it.id == id }?.let { word ->
                quizzes.add(word)
                println("Word added to drawing quiz: $quizzes")
            }
        }

    }

    fun onSendWordsToQuiz(){
        quizzes.clear()
        selectedWords.forEach { id->
            words.find{it.id==id}?.let{word->
                quizzes.add(word)
                println("Word added to quiz: $quizzes")

            }
        }
    }



}