package com.vivek.notes.core.domain.model

import com.vivek.notes.core.data.local.entity.NoteEntity

data class Note(
    val id: Int,
    val title: String,
    val description: String
)


fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = id.takeIf { it != -1 },
        title = title,
        description = description
    )
}

fun dummyNotes(): ArrayList<Note> {
    val notes = ArrayList<Note>()
    for (i in 1..3) {
        notes.add(Note(i, "Title $i", "Description $i"))
    }
    return notes
}