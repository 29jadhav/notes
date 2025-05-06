package com.vivek.notes.features.home_feature.domain

import com.google.common.truth.Truth
import com.vivek.notes.core.data.local.MockNotesDao
import com.vivek.notes.core.data.local.entity.NoteEntity
import com.vivek.notes.core.data.local.entity.toModel
import com.vivek.notes.core.data.repository.NotesRepositoryImpl
import com.vivek.notes.core.domain.repository.NotesRepository
import com.vivek.notes.features.home_feature.event.NotesEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

private val note1 = NoteEntity(1, "Note 1  title", "My 1 note")
private val note2 = NoteEntity(2, "Note 2  title", "My 2 note")
private val note3 = NoteEntity(3, "Note 3  title", "My 3 note")
private val note4 = NoteEntity(null, "Note 4  title", "My 4 note")

class NotesEventListenUseCaseTest {

    private lateinit var repository: NotesRepository
    private lateinit var notesDao: MockNotesDao
    private lateinit var notesEventListenUseCase: NotesEventListenUseCase
    private lateinit var notes: ArrayList<NoteEntity>

    @Before
    fun setUp() {
        notes = arrayListOf(note1, note2, note3)
        notesDao = MockNotesDao(notes)
        repository = NotesRepositoryImpl(notesDao)
        notesEventListenUseCase = NotesEventListenUseCase(repository)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun listenNotes_addNote_returnsAddNotesEvent() = runTest {

        val notesEvent = mutableListOf<NotesEvent>()
        val job= backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            notesEventListenUseCase.execute().collect {
                notesEvent.add(it)
            }
        }
        advanceUntilIdle()
        repository.addNote(note4.toModel())
        job.cancel()
        val result = notesEvent.firstOrNull()

        Truth.assertThat((result as NotesEvent)::class)
            .isEqualTo(NotesEvent.NewNote::class)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun listenNotes_updateNote_returnsUpdatedNotesEvent() = runTest {
        val notesEvent = mutableListOf<NotesEvent>()
        val job= backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            notesEventListenUseCase.execute().collect {
                notesEvent.add(it)
            }
        }
        advanceUntilIdle()
        repository.updateNote(note1.toModel().copy(description = "This is updated description"))
        job.cancel()
        val result = notesEvent.firstOrNull()

        Truth.assertThat((result as NotesEvent).javaClass.simpleName)
            .isEqualTo(NotesEvent.UpdateNote::class.simpleName)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun listenNotes_deleteNote_returnsDeleteNoteEvent() = runTest {
        val notesEvent = mutableListOf<NotesEvent>()
        val job=backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            notesEventListenUseCase.execute().collect {
                notesEvent.add(it)
            }
        }
        advanceUntilIdle()
        repository.deleteNote(note1.toModel())
        job.cancel()
        val result = notesEvent.firstOrNull()

        Truth.assertThat((result as NotesEvent).javaClass.simpleName)
            .isEqualTo(NotesEvent.DeleteNote::class.simpleName)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun listenNotes_multipleActions_emitsInOrder() = runTest {
        val events = mutableListOf<NotesEvent>()

        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            notesEventListenUseCase.execute().collect {
                events.add(it)
            }
        }

        // Let collector start first!
        advanceUntilIdle()

        // Now perform actions
        repository.addNote(note4.toModel())
        advanceUntilIdle()

        repository.updateNote(note1.toModel().copy(description = "Updated!"))
        advanceUntilIdle()

        repository.deleteNote(note2.toModel())
        advanceUntilIdle()

        job.cancel()

        // Now check all 3 events came through, in order
        Truth.assertThat(events.map { it::class }).containsExactly(
            NotesEvent.NewNote::class,
            NotesEvent.UpdateNote::class,
            NotesEvent.DeleteNote::class
        ).inOrder()
    }
}