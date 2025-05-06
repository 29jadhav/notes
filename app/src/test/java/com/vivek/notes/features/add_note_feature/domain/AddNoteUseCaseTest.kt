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
private val note4 = NoteEntity(null, "Note 4  title", "My 4 note")
private val note5 = NoteEntity(null, "", "")

class AddNoteUseCaseTest {
    private lateinit var mockNotesDao: MockNotesDao
    private lateinit var repository: NotesRepository
    private lateinit var addNoteUseCase: AddNoteUseCase
    private lateinit var notes: ArrayList<NoteEntity>

    @Before
    fun setUp() {
        notes = arrayListOf(note1, note2, note3)
        mockNotesDao = MockNotesDao(notes)
        repository = NotesRepositoryImpl(mockNotesDao)
        addNoteUseCase = AddNoteUseCase(repository)
    }


    @Test
    fun addNoteUseCase_addNote_returnAddedNote() = runTest {
        val newNote = note4.toModel()
        addNoteUseCase.execute(newNote)

        Truth.assertThat(notes.size).isEqualTo(4)
    }

    @Test
    fun addNoteUseCase_updateNote_returnsUpdatedNote() = runTest {
        val updatedNote1 = note1.toModel()
            .copy(title = "Updated note title", description = "Updated note description")

        addNoteUseCase.execute(updatedNote1)

       val noteFromDao =  repository.getNoteById(updatedNote1.id)
        Truth.assertThat(noteFromDao).isEqualTo(updatedNote1)
    }


    @Test
    fun addNoteUseCase_emptyNote_noteShouldNotBeCreated()= runTest {
        val newNote = note5.toModel()
        addNoteUseCase.execute(newNote)

        Truth.assertThat(notes.size).isEqualTo(3)
    }
}