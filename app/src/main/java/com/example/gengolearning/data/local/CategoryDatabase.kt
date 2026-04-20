package com.example.gengolearning.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.gengolearning.model.appmodels.WordCategories
import kotlinx.coroutines.flow.Flow
@Dao
interface CategoryDatabase {

    @Upsert
    suspend fun upsertCategory(wordCategory: WordCategories)

    @Upsert
    suspend fun upsertCategories(list: List<WordCategories>)




    @Query
    ("SELECT * FROM wordcategories WHERE language= :language ORDER BY categoryName ASC")
    fun getAllCategories(language: String): Flow<List<WordCategories>>

    @Delete
    suspend fun deleteCategory(wordCategory: WordCategories)


  @Query
  ("DELETE FROM wordcategories")
  suspend fun clearCategories()


}