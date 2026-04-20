package com.example.gengolearning.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gengolearning.model.appmodels.Grammar
import com.example.gengolearning.model.appmodels.ProfilePicture
import com.example.gengolearning.model.appmodels.WordCategories
import com.example.gengolearning.model.appmodels.Words
import com.example.gengolearning.model.utils.Converters

@Database(
    entities = [Words::class, Grammar::class, ProfilePicture:: class, WordCategories:: class],
    version = 7
)
@TypeConverters(Converters::class)
abstract class WordsDatabase: RoomDatabase() {
    abstract val dao: WordsDao
    abstract val grammarDao: GrammarDao

    abstract val profileDao: ProfileDao

    abstract val categoryDao: CategoryDatabase



}

