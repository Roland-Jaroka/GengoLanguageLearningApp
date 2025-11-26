package com.example.gengolearning.grammar

import com.example.gengolearning.model.Grammar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object LanguageGrammar {
    private val _grammar = MutableStateFlow<List<Grammar>>(emptyList())
    val grammar: StateFlow<List<Grammar>> = _grammar
    private val grammarCatch= mutableMapOf<String, List<Grammar>>()

    fun loadGrammar(language: String){
        val auth= FirebaseAuth.getInstance()
        val uid= auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return

        _grammar.value = grammarCatch[language] ?: emptyList()

        Firebase.firestore
            .collection("users")
            .document(uid)
            .collection(language)
            .document(language)
            .collection("grammar")
            .get().addOnSuccessListener{
                val list = mutableListOf<Grammar>()
                for(document in it) {
                    val grammar = document.getString("grammar") ?: ""
                    val explanation = document.getString("explanation") ?: ""
                    val examples = document.get("examples") as? List<String> ?: emptyList()
                    val id = document.id
                    list.add(Grammar(grammar, explanation, examples, id))
                }
                grammarCatch[language] = list
                _grammar.value = list
            }
    }

    fun addGrammar(language: String, example: String, grammar: Grammar){
        val auth= FirebaseAuth.getInstance()
        val uid= auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return

        val allExamples= mutableListOf<String>()
        if (example.isNotBlank()) allExamples.add(example)
        allExamples.addAll(grammar.examples?.filter { it.isNotBlank() } ?: emptyList())

        Firebase.firestore
            .collection("users")
            .document(uid)
            .collection(language)
            .document(language)
            .collection("grammar")
            .add(
                mapOf(
                    "grammar" to grammar.grammar,
                    "explanation" to grammar.explanation,
                    "examples" to allExamples
                )
            )
    }

    fun addNewExample(language: String, grammarid: String?, exampleText: String) {

        if (exampleText.isBlank()) return

        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid


        Firebase.firestore
            .collection("users")
            .document(uid!!)
            .collection(language)
            .document(language)
            .collection("grammar")
            .document(grammarid!!)
            .update("examples", FieldValue.arrayUnion(exampleText))

           _grammar.value = _grammar.value.map { grammar->
               if (grammar.id == grammarid){
                   grammar.copy(examples = grammar.examples?.plus(exampleText))
               } else grammar
           }
    }

    fun onSave(language: String, grammarid: String?, grammarText: String, explanation: String){
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        Firebase.firestore
            .collection("users")
            .document(uid!!)
            .collection(language)
            .document(language)
            .collection("grammar")
            .document(grammarid!!)
            .update(
                mapOf(
                    "grammar" to grammarText,
                    "explanation" to explanation
                )
            )
        _grammar.value = _grammar.value.map{grammar->
            if (grammar.id == grammarid){
                grammar.copy(grammar = grammarText, explanation = explanation)
            } else grammar
        }
    }

    fun onExampleRemove(language: String, grammarId: String?, exampleRows: List<String>, index: Int) {
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        Firebase.firestore
            .collection("users")
            .document(uid!!)
            .collection(language)
            .document(language)
            .collection("grammar")
            .document(grammarId!!)
            .update(
                "examples", FieldValue.arrayRemove(exampleRows[index])
            )

        _grammar.value= _grammar.value.map{grammar->
            if (grammar.id == grammarId) {
                grammar.copy(examples= grammar.examples?.minus(exampleRows[index]) )
            } else grammar
        }
    }

    fun onRemove(language: String, grammarId: String?){
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        _grammar.value = _grammar.value.filterNot { it.id == grammarId }

        Firebase.firestore
            .collection("users")
            .document(uid!!)
            .collection(language)
            .document(language)
            .collection("grammar")
            .document(grammarId!!)
            .delete()




    }



}