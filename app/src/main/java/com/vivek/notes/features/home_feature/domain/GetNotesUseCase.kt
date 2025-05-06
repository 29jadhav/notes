package com.vivek.notes.features.home_feature.domain

import com.vivek.notes.core.domain.model.Note
import com.vivek.notes.core.domain.repository.NotesRepository

class GetNotesUseCase(private val notesRepository: NotesRepository) {

    suspend fun execute(): List<Note> {
        return notesRepository.getAll()
    }
}