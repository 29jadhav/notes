package com.vivek.notes.core.data.repository

import com.google.common.truth.Truth
import com.vivek.notes.core.data.local.MockNotesDao
import com.vivek.notes.core.data.local.entity.NoteEntity
import com.vivek.notes.core.data.local.entity.toModel
import com.vivek.notes.core.domain.model.Note
import com.vivek.notes.core.domain.repository.NotesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


private val note1 = NoteEntity(1, "Note 1  title", "My 1 note")
private val note2 = NoteEntity(2, "Note 2  title", "My 2 note")
private val note3 = NoteEntity(3, "Note 3  title", "My 3 note")
private val note4 = NoteEntity(null, "Note 4  title", "My 4 note")


class NotesRepositoryImplTest {
    private lateinit var notesDao: MockNotesDao
    private lateinit var repository: NotesRepository


    @Before
    fun setup() {
        //Arrange
        val allItems = arrayListOf(note1, note2, note3)
        notesDao = MockNotesDao(allItems)
        repository = NotesRepositoryImpl(notesDao)
    }

    @Test
    fun getAll_returns3Items() = runTest {
        val result = repository.getAll()

        Truth.assertThat(result.size).isEqualTo(3)
    }

    @Test
    fun getAll_deleteNote_return2Items() = runTest {

        repository.deleteNote(note1.toModel())
        val result = repository.getAll()
        Truth.assertThat(result.size).isEqualTo(2)

    }

    @Test
    fun getAll_addNote_returns4Items() = runTest {
        repository.addNote(note4.toModel())

        val result = repository.getAll()
        Truth.assertThat(result.size).isEqualTo(4)
    }

    @Test
    fun getNote_return1Item() = runTest {
        val result = repository.getNoteById(note1.toModel().id)

        Truth.assertThat(result).isEqualTo(note1.toModel())
    }


    @Test
    fun getNote_invalidNoteId_returnNone() = runTest {
        val result = repository.getNoteById(10)
        Truth.assertThat(result).isEqualTo(null)
    }

    @Test
    fun addNote_newNote_returnNewNoteWithId() = runTest {
        val note = note4.toModel()
        val noteId = repository.addNote(note)
        val addedNote = note.copy(id = noteId.toInt())
        val noteFromDao = repository.getNoteById(noteId.toInt())

        Truth.assertThat(addedNote).isEqualTo(noteFromDao)

    }

    @Test
    fun updateNote_returnUpdateNote() = runTest {
        val updatedNote =
            note1.copy(title = "This is updated title", description = "This is updated description")
                .toModel()
        repository.updateNote(updatedNote)

        val result = repository.getNoteById(updatedNote.id)

        Truth.assertThat(result).isEqualTo(updatedNote)
    }


    @Test
    fun deleteNote_returnsNull() = runTest {
        val note = note1.toModel()
        repository.deleteNote(note)

        val result = repository.getNoteById(note.id)
        Truth.assertThat(result).isEqualTo(null)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addNoteListener_returns2AddedItems() = runTest {

        val notes = mutableListOf<Note>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.addNoteListener.collect {
                notes.add(it)
            }
        }
        val noteModel1 = note1.toModel()
        val note1Id = repository.addNote(noteModel1)
        val updatedNote1 = noteModel1.copy(id = note1Id.toInt())


        val noteModel2 = note2.toModel()
        val note2Id = repository.addNote(noteModel2)
        val updatedNote2 = noteModel2.copy(id = note2Id.toInt())

        Truth.assertThat(notes[0]).isEqualTo(updatedNote1)
        Truth.assertThat(notes[1]).isEqualTo(updatedNote2)
        Truth.assertThat(notes.size).isEqualTo(2)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun updateNoteListener_returns2UpdatedItems() = runTest {
        val notes = mutableListOf<Note>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.updateNoteListener.collect {
                notes.add(it)
            }
        }

        val updatedNote1 = note1.toModel()
            .copy(title = "Updated note 1 title", description = "updated note 1 description")
        repository.updateNote(updatedNote1)
        val updatedNote2 = note2.toModel()
            .copy(title = "Updated note 2 title", description = "updated note 2 description")
        repository.updateNote(updatedNote2)

        Truth.assertThat(notes[0]).isEqualTo(updatedNote1)
        Truth.assertThat(notes[1]).isEqualTo(updatedNote2)
        Truth.assertThat(notes.size).isEqualTo(2)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteNoteListener_return2DeletedItems() = runTest {

        val notes = mutableListOf<Note>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.deleteNoteListener.collect {
                notes.add(it)
            }
        }

        repository.deleteNote(note1.toModel())
        repository.deleteNote(note2.toModel())

        Truth.assertThat(notes[0]).isEqualTo(note1.toModel())
        Truth.assertThat(notes[1]).isEqualTo(note2.toModel())
        Truth.assertThat(notes.size).isEqualTo(2)
    }
}