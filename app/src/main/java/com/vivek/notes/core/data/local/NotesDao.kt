package com.vivek.notes.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.vivek.notes.core.data.local.entity.NoteEntity

@Dao
interface NotesDao {

    @Query("SELECT * FROM notes")
    fun getAllNotes(): List<NoteEntity>

    @Update
    fun updateNote(note: NoteEntity): Int

    @Delete
    fun deleteNote(note: NoteEntity)

    @Insert
    fun addNote(note: NoteEntity): Long

    @Query("SELECT * FROM notes WHERE id=:id")
    fun getNoteById(id: Int): NoteEntity?

}