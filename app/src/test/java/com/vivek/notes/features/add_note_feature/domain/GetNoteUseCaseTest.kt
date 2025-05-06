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

class GetNoteUseCaseTest {
    private lateinit var mockNotesDao: MockNotesDao
    private lateinit var repository: NotesRepository
    private lateinit var getNoteUseCase: GetNoteUseCase
    private lateinit var notes: ArrayList<NoteEntity>

    @Before
    fun setUp() {
        notes = arrayListOf(note1, note2, note3)
        mockNotesDao = MockNotesDao(notes)
        repository = NotesRepositoryImpl(mockNotesDao)
        getNoteUseCase = GetNoteUseCase(repository)
    }


    @Test
    fun getNoteUseCase_noteId_returnsNote() = runTest {

        val note2Note = note2.toModel()
        val note = getNoteUseCase.execute(note2Note.id)

        Truth.assertThat(note).isEqualTo(note2Note)
    }

    @Test
    fun getNoteUseCase_invalidNoteId_returnsNull() = runTest {

        val note = getNoteUseCase.execute(15)

        Truth.assertThat(note).isEqualTo(null)
    }
}