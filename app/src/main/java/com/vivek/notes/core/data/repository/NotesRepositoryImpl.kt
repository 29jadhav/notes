package com.vivek.notes.core.data.repository

import com.vivek.notes.core.data.local.NotesDao
import com.vivek.notes.core.data.local.entity.toModel
import com.vivek.notes.core.domain.model.Note
import com.vivek.notes.core.domain.model.toEntity
import com.vivek.notes.core.domain.repository.NotesRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(private val notesDao: NotesDao) :
    NotesRepository {

    private val _addNoteListener = MutableSharedFlow<Note>()
    override val addNoteListener = _addNoteListener.asSharedFlow()

    private val _updateNoteListener = MutableSharedFlow<Note>()
    override val updateNoteListener = _updateNoteListener.asSharedFlow()

    private val _deleteNoteListener = MutableSharedFlow<Note>()
    override val deleteNoteListener = _deleteNoteListener.asSharedFlow()

    override suspend fun getAll(): List<Note> {
        return notesDao.getAllNotes().map { it.toModel() }
    }


    override suspend fun updateNote(note: Note): Boolean {
        val index = notesDao.updateNote(note.toEntity())
        _updateNoteListener.emit(note)
        return index > 0
    }

    override suspend fun addNote(note: Note): Long {
        val id = notesDao.addNote(note.toEntity())
        val newNote = note.copy(id = id.toInt())
        _addNoteListener.emit(newNote)
        return id
    }

    override suspend fun getNoteById(id: Int): Note? {
        return notesDao.getNoteById(id)?.toModel()
    }

    override suspend fun deleteNote(note: Note): Boolean {
        notesDao.deleteNote(note.toEntity())
        _deleteNoteListener.emit(note)
        return true

    }
}