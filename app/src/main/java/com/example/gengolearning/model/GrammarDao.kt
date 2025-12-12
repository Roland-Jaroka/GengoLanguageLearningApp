package com.example.gengolearning.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface GrammarDao {
    @Query("SELECT * FROM grammar WHERE language= :language ORDER BY Grammar ASC")
    fun getAllGrammar(language: String): Flow<List<Grammar>>
    @Upsert
    suspend fun upsertGrammar(list: List<Grammar>)

    @Upsert
    suspend fun uploadGrammar(grammar: Grammar)

    @Query("DELETE FROM grammar WHERE id= :id")
    suspend fun deleteGrammar(id: String)

    @Delete
    suspend fun deleteGrammar(grammar: Grammar)

   @Update
   suspend fun updateGrammar(grammar: Grammar)


}