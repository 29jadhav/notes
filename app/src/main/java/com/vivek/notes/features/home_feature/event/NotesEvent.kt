package com.vivek.notes.features.home_feature.event

import com.vivek.notes.core.domain.model.Note

sealed class NotesEvent {
    data class NewNote(val note: Note): NotesEvent()
    data class DeleteNote(val note: Note): NotesEvent()
    data class UpdateNote(val note: Note): NotesEvent()
}