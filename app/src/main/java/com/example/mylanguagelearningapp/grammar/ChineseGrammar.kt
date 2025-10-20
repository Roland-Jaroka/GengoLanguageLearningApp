package com.example.mylanguagelearningapp.grammar

import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import com.example.mylanguagelearningapp.model.Grammar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

object ChineseGrammar {
    val grammarList = mutableStateListOf<Grammar>()

    fun loadGrammar(){
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val uid = auth.currentUser?.uid

        db.collection("users")
            .document(uid!!)
            .collection("chineseCollection")
            .document("chinese")
            .collection("chinese_grammar")
            .get()
            .addOnSuccessListener {
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