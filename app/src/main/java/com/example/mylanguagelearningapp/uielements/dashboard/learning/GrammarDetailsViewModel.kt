package com.example.mylanguagelearningapp.uielements.dashboard.learning

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mylanguagelearningapp.grammar.JapaneseGrammar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class GrammarDetailsViewModel: ViewModel() {


    fun addNewExample(grammarid: String?, exampleText: String) {

        if (exampleText.isBlank()) return

        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .document(uid!!)
            .collection("grammar")
            .document(grammarid!!)
            .update("examples", FieldValue.arrayUnion(exampleText))

    }

    fun onSave(grammarid: String?, grammar: String, explanation: String){
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .document(uid!!)
            .collection("grammar")
            .document(grammarid!!)
            .update(mapOf(
                "grammar" to grammar,
                "explanation" to explanation
            )).addOnSuccessListener {
                JapaneseGrammar.loadGrammar()
            }

    }

    fun onExampleDelete(grammarId: String?, exampleRows: List<String>, index: Int) {
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .document(uid!!)
            .collection("grammar")
            .document(grammarId!!)
            .update("examples", FieldValue.arrayRemove(exampleRows[index]))
            .addOnSuccessListener { JapaneseGrammar.loadGrammar() }

    }

    fun onRemove(grammarid: String?){
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .document(uid!!)
            .collection("grammar")
            .document(grammarid!!)
            .delete()
    }
}