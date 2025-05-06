package com.vivek.notes.core.domain.repository

import com.vivek.notes.core.domain.model.Note
import kotlinx.coroutines.flow.SharedFlow

interface NotesRepository {
    val addNoteListener: SharedFlow<Note>
    val updateNoteListener: SharedFlow<Note>
    val deleteNoteListener: SharedFlow<Note>

    suspend fun getAll(): List<Note>

    suspend fun updateNote(note: Note): Boolean

    suspend fun addNote(note: Note): Long

    suspend fun getNoteById(id: Int): Note?

    suspend fun deleteNote(note: Note): Boolean
}