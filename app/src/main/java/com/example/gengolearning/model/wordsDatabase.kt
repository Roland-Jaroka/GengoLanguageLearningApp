package com.example.gengolearning.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface WordsDao {

    @Upsert
    suspend fun updateWords(words: Words)

    @Delete
    suspend fun deleteWords(words: Words)

    @Query("SELECT * FROM words ORDER BY word ASC")
    fun getAllWords(): Flow<List<Words>>

}