package com.vivek.notes.features.add_note_feature.event

sealed class AddNoteEvent {
    data object OnBackPress : AddNoteEvent()
}

sealed class AddNoteAction{
    data class OnTitleChange(val title: String): AddNoteAction()
    data class OnDescriptionChange(val description: String): AddNoteAction()
    data object OnDeleteNote : AddNoteAction()
}