package com.example.mylanguagelearningapp.uielements.dashboard.learning

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddNewGrammarViewModel: ViewModel() {

    var grammar by mutableStateOf("")
        private set

    fun onGrammarInputChange(newValue: String) {

        grammar= newValue

    }

    var explanation by mutableStateOf("")
        private set

    fun onExplanationInputChange(newValue: String) {
        explanation= newValue
    }

    var example by mutableStateOf("")
        private set

    fun onFirstExampleChange(newValue: String){
        example= newValue

    }

    val examplerows = mutableStateListOf<String>()

    fun addGrammarToList() {
      val auth = FirebaseAuth.getInstance()
      val db = FirebaseFirestore.getInstance()
        val uid = auth.currentUser?.uid

        val allExamples = mutableListOf<String>()
        if (example.isNotBlank()) allExamples.add(example)
        allExamples.addAll(examplerows.filter { it.isNotBlank() })

        db.collection("users")
            .document(uid!!)
            .collection("grammar")
            .add(
                mapOf(
                    "grammar" to grammar,
                    "explanation" to explanation,
                    "examples" to allExamples

                )
            ).addOnSuccessListener {
                grammar = ""
                explanation = ""
                example = ""
                examplerows.clear()


            }

    }


}