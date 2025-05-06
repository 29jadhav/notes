package com.vivek.notes.features.add_note_feature.domain

import com.google.common.truth.Truth
import com.vivek.notes.core.data.local.MockNotesDao
import com.vivek.notes.core.data.local.entity.NoteEntity
import com.vivek.notes.core.data.local.entity.toModel
import com.vivek.notes.core.data.repository.NotesRepositoryImpl
import com.vivek.notes.core.domain.repository.NotesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

private val note1 = NoteEntity(1, "Note 1  title", "My 1 note")
private val note2 = NoteEntity(2, "Note 2  title", "My 2 note")
private val note3 = NoteEntity(3, "Note 3  title", "My 3 note")

class DeleteNoteUseCaseTest {
    private lateinit var mockNotesDao: MockNotesDao
    private lateinit var repository: NotesRepository
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase
    private lateinit var notes: ArrayList<NoteEntity>


    @Before
    fun setUp() {
        notes = arrayListOf(note1, note2, note3)
        mockNotesDao = MockNotesDao(notes)
        repository = NotesRepositoryImpl(mockNotesDao)
        deleteNoteUseCase = DeleteNoteUseCase(repository)
    }


    @Test
    fun deleteNoteUseCase_noteId_deleteNote() = runTest {

        val note = note1.toModel()
        val isDeleted = deleteNoteUseCase.execute(note.id)

        Truth.assertThat(isDeleted).isEqualTo(true)
        Truth.assertThat(notes.size).isEqualTo(2)

    }

    @Test
    fun deleteNoteUseCase_invalidId_returnsFalse()= runTest {

        val isDeleted = deleteNoteUseCase.execute(10)
        Truth.assertThat(isDeleted).isEqualTo(false)
    }
}