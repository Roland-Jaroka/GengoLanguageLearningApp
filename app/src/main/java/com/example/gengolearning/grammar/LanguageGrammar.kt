package com.example.gengolearning.grammar

import com.example.gengolearning.model.Grammar
import com.example.gengolearning.model.GrammarDao
import com.example.gengolearning.model.UserSettingsRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await

class LanguageGrammar @Inject constructor(
    private val dao: GrammarDao,
    private val userSettingsRepository: UserSettingsRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    val grammar: Flow<List<Grammar>> = userSettingsRepository.selectedLanguage.flatMapLatest{ language->
        dao.getAllGrammar(language.code)
    }

 suspend   fun loadGrammar(language: String){
        val auth= FirebaseAuth.getInstance()
        val uid= auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return


        val result= Firebase.firestore
            .collection("users")
            .document(uid)
            .collection(language)
            .document(language)
            .collection("grammar")
            .get().await()
                val list = mutableListOf<Grammar>()
                for(document in result) {
                    val grammar = document.getString("grammar") ?: ""
                    val explanation = document.getString("explanation") ?: ""
                    val examples = document.get("examples") as? List<String> ?: emptyList()
                    val id = document.id
                    list.add(Grammar(grammar, explanation, examples, id, language = language))
                }
                  dao.upsertGrammar(list)

    }

    suspend fun addGrammar(language: String, grammar: Grammar){
        val auth= FirebaseAuth.getInstance()
        val uid= auth.currentUser?.uid.toString()
        if (uid.isEmpty()) return

         val docID = grammar.id


        dao.uploadGrammar(grammar)

        Firebase.firestore
            .collection("users")
            .document(uid)
            .collection(language)
            .document(language)
            .collection("grammar")
            .document(docID)
            .set(
                mapOf(
                    "grammar" to grammar.grammar,
                    "explanation" to grammar.explanation,
                    "examples" to grammar.examples,
                    "langauge" to grammar.language
                )
            )
    }

   suspend fun addNewExample(language: String, grammarid: String?, exampleText: String, grammar: Grammar?) {

        if (exampleText.isBlank()) return
       if (grammar == null) return

        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        //Update in Room
        val currentList = grammar.examples
        val newList = currentList?.plus(exampleText)
        dao.updateGrammar(grammar.copy(examples = newList))


        //update in Firebase
        Firebase.firestore
            .collection("users")
            .document(uid!!)
            .collection(language)
            .document(language)
            .collection("grammar")
            .document(grammarid!!)
            .update("examples", FieldValue.arrayUnion(exampleText))

    }

   suspend fun onExampleRemove(language: String, grammarId: String?, exampleRows: List<String>, index: Int, grammar: Grammar?) {

        if (grammar == null) return

        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        //Delete in Room
        val currentList= grammar.examples
        val newList = currentList?.minus(exampleRows[index])
        dao.updateGrammar(grammar.copy(examples = newList))


       //Delete in Firebase
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

    }

   suspend fun onSave(language: String, grammarid: String?, grammarText: String, explanation: String, grammar: Grammar?){
       if (grammar == null) return
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        dao.updateGrammar(grammar.copy(grammar = grammarText, explanation = explanation))

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
    }



   suspend fun onRemove(language: String, grammarId: String){
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        dao.deleteGrammar(Grammar(id = grammarId))


        Firebase.firestore
            .collection("users")
            .document(uid!!)
            .collection(language)
            .document(language)
            .collection("grammar")
            .document(grammarId)
            .delete()




    }



}