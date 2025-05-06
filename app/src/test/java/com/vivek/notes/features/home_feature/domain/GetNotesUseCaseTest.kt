package com.vivek.notes.features.home_feature.domain

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

class GetNotesUseCaseTest {
    private lateinit var repository: NotesRepository
    private lateinit var notesDao: MockNotesDao
    private lateinit var getNotesUseCase: GetNotesUseCase
    private lateinit var notes: ArrayList<NoteEntity>

    @Before
    fun setUp() {
        notes = arrayListOf(note1, note2, note3)
        notesDao = MockNotesDao(notes)
        repository = NotesRepositoryImpl(notesDao)
        getNotesUseCase = GetNotesUseCase(repository)
    }

    @Test
    fun getAllNotes_returns3Notes() = runTest {
        val notesFromDao = getNotesUseCase.execute()

        Truth.assertThat(notesFromDao.size).isEqualTo(notes.size)
    }


    @Test
    fun getAllNotes_deleteNote_returns2Notes() = runTest {
        repository.deleteNote(note1.toModel())

        val notesFromDao = getNotesUseCase.execute()
        Truth.assertThat(notesFromDao.size).isEqualTo(2)
    }

    @Test
    fun getAllNotes_addNote_return4Notes() = runTest {

        repository.addNote(note4.toModel())
        val notesFromDao = getNotesUseCase.execute()
        Truth.assertThat(notesFromDao.size).isEqualTo(4)
    }

    @Test
    fun getAllNotes_updateNote_return3Items() = runTest {
        val updatedNote1 = note1.toModel().copy(title = "This is updated title")
        repository.updateNote(updatedNote1)

        val notesFromDao = getNotesUseCase.execute()
        val updatedNote = notesFromDao.first { it.id == updatedNote1.id }

        Truth.assertThat(updatedNote).isEqualTo(updatedNote1)
        Truth.assertThat(notesFromDao.size).isEqualTo(3)

    }
}