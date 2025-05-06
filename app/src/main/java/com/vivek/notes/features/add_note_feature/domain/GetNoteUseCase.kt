package com.vivek.notes.features.add_note_feature.domain

import com.vivek.notes.core.domain.model.Note
import com.vivek.notes.core.domain.repository.NotesRepository

class GetNoteUseCase(private val notesRepository: NotesRepository) {

    suspend fun execute(noteId: Int): Note? {
        return notesRepository.getNoteById(noteId)
    }
}