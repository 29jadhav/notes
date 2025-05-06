package com.vivek.notes.core.data.local

import androidx.room.Database
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase
import com.vivek.notes.core.data.local.entity.NoteEntity

@Database(entities = [NoteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun notesDao(): NotesDao

    override fun clearAllTables() {
        TODO("Not yet implemented")
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        TODO("Not yet implemented")
    }
}