package com.example.gengolearning.words

import android.content.Context
import androidx.room.Room
import com.example.gengolearning.model.Words
import com.example.gengolearning.model.WordsDatabase


class WordsRepository(context: Context) {

    private val db= Room.databaseBuilder(
        context.applicationContext,
        WordsDatabase::class.java,
        "word_database").build()

    private val dao = db.dao

    suspend fun addWord(words: Words){
        dao.updateWords(words)
    }
}