package com.example.gengolearning.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Words::class],
    version = 1
)

abstract class WordsDatabase: RoomDatabase() {

    abstract val dao: WordsDao

}
