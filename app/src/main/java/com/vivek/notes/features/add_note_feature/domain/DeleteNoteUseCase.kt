package com.vivek.notes.features.add_note_feature.domain

import com.vivek.notes.core.domain.repository.NotesRepository

class DeleteNoteUseCase(private val notesRepository: NotesRepository) {

    suspend fun execute(noteId: Int): Boolean {
        val note = notesRepository.getNoteById(noteId)
        return note?.let { notesRepository.deleteNote(it) } == true
    }
}