package com.vivek.notes.core.data.local

import com.vivek.notes.core.data.local.entity.NoteEntity

class MockNotesDao(private val items: ArrayList<NoteEntity>) : NotesDao {
    override fun getAllNotes(): List<NoteEntity> {
        return items
    }

    override fun updateNote(note: NoteEntity): Int {
        val index = items.indexOfFirst { it.id == note.id }
        if (index != -1) items[index] = note
        return index
    }

    override fun deleteNote(note: NoteEntity) {
        items.remove(note)
    }

    override fun addNote(note: NoteEntity): Long {
        val id = items.size + 1
        val noteWithId = note.copy(id = id)
        items.add(noteWithId)
        return id.toLong()
    }

    override fun getNoteById(id: Int): NoteEntity? {
        val index = items.indexOfFirst { it.id == id }
        if (index == -1) return null
        return items[index]
    }
}