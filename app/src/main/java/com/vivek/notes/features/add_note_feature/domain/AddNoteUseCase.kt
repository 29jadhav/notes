package com.vivek.notes.features.add_note_feature.domain

import com.vivek.notes.core.domain.model.Note
import com.vivek.notes.core.domain.model.toEntity
import com.vivek.notes.core.data.repository.NotesRepositoryImpl
import com.vivek.notes.core.domain.repository.NotesRepository
import javax.inject.Inject

class AddNoteUseCase(private val notesRepository: NotesRepository) {

    suspend fun execute(note: Note) {
        if (note.title.isNotBlank() || note.description.isNotBlank()) {
            if (note.id == -1)
                notesRepository.addNote(note)
            else
                notesRepository.updateNote(note)
        }
    }
}