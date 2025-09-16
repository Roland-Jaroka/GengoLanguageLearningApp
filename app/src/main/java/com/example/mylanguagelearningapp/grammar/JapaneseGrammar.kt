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

    fun addGrammar(grammar: Grammar, example: String){
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val uid = auth.currentUser?.uid

        val allExamples = mutableListOf<String>()
        if (example.isNotBlank()) allExamples.add(example)
        allExamples.addAll(grammar.examples?.filter { it.isNotBlank() } ?: emptyList())

        db.collection("users")
            .document(uid!!)
            .collection("grammar")
            .add(
                mapOf(
                    "grammar" to grammar.grammar,
                    "explanation" to grammar.explanation,
                    "examples" to allExamples
                )
            )

    }

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
                loadGrammar()
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
            .addOnSuccessListener { loadGrammar() }

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