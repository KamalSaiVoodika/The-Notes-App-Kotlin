package com.example.thenotesappkotlin.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.thenotesappkotlin.model.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao
    //The companion object is used to create a singleton instance of the NoteDatabase class.
    // The instance variable is marked as @Volatile to ensure that writes to this field are immediately visible to other threads. The LOCK object is used to synchronize access to the instance variable.
    companion object {
        @Volatile
        private var instance: NoteDatabase? = null
        private val LOCK = Any()

        //The invoke() function is used to create the database instance if it does not already exist. If the instance already exists, it is returned. The synchronized block ensures that only one thread can create the database instance at a time.
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, NoteDatabase::class.java, "NOTE_DB")
                .build()

    }
}