package com.example.gengolearning.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gengolearning.model.utils.Converters
import com.example.gengolearning.model.appmodels.Grammar
import com.example.gengolearning.model.appmodels.Words

@Database(
    entities = [Words::class, Grammar::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class WordsDatabase: RoomDatabase() {
    abstract val dao: WordsDao
    abstract val grammarDao: GrammarDao


}

