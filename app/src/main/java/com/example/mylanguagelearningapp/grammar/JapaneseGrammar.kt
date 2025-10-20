package com.example.mylanguagelearningapp.grammar

import androidx.compose.runtime.mutableStateListOf
import com.example.mylanguagelearningapp.model.Grammar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

object JapaneseGrammar {

    val grammarList = mutableStateListOf<Grammar>()

    val auth= FirebaseAuth.getInstance()


    fun loadGrammar() {
        val uid= auth.currentUser?.uid.toString()

        Firebase.firestore
            .collection("users")
            .document(uid)
            .collection("grammar")
            .get().addOnSuccessListener {
                grammarList.clear()

                for (document in it) {
                    val grammar = document.getString("grammar") ?: ""
                    val explanation = document.getString("explanation") ?: ""
                    val examples = document.get("examples") as? List<String> ?: emptyList()
                    val id = document.id
                    grammarList.add(Grammar(grammar, explanation, examples, id))
                }
            }
    }


}