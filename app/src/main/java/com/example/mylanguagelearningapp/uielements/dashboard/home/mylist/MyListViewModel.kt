package com.example.mylanguagelearningapp.uielements.dashboard.home.mylist

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mylanguagelearningapp.japanesewords.JapaneseWords
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyListViewModel: ViewModel() {
    val words = JapaneseWords.wordList

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
        selectedWords.forEach{ id ->
            db.collection("users")
                .document(uid)
                .collection("words")
                .document(id)
                .delete()
                .addOnSuccessListener {
                    selectedWords.clear()
                    JapaneseWords.loadWords()
                    println("Word removed from database")
                }
        }

    }



}