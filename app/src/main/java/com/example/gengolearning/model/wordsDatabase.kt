package com.example.gengolearning.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface WordsDao {

    @Upsert
    suspend fun updateWords(words: Words)

    @Upsert
    suspend fun upsertWords(list: List<Words>)

    @Query("UPDATE words SET isOnHomePage = :isOnHomePage WHERE id = :id")
    suspend fun updateIsOnHomePage(id: String, isOnHomePage: Boolean)

    @Update
    suspend fun updateWord(word: Words)


    @Delete
    suspend fun deleteWords(words: Words)

    @Query("SELECT * FROM words WHERE language = :language ORDER BY word ASC")
    fun getAllWords(language: String): Flow<List<Words>>

    //Get the number of words in the database
    @Query("SELECT COUNT(*) FROM words")
   suspend fun getWordCount(): Int

   //Get the number of languages that has a word at least in database
   @Query("SELECT COUNT(DISTINCT language) FROM words")
   suspend fun getLanguageCount(): Int



}