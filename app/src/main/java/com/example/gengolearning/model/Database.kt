package com.example.gengolearning.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Words::class, Grammar::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class WordsDatabase: RoomDatabase() {
    abstract val dao: WordsDao
    abstract val grammarDao: GrammarDao


}

