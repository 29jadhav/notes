package com.vivek.notes.features.home_feature.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.vivek.notes.DummyNotesEntity
import com.vivek.notes.core.data.local.MockNotesDao
import com.vivek.notes.core.data.local.entity.NoteEntity
import com.vivek.notes.core.data.local.entity.toModel
import com.vivek.notes.core.data.repository.NotesRepositoryImpl
import com.vivek.notes.core.domain.repository.NotesRepository
import com.vivek.notes.features.home_feature.domain.GetNotesUseCase
import com.vivek.notes.features.home_feature.domain.NotesEventListenUseCase
import com.vivek.notes.features.home_feature.event.HomeAction
import com.vivek.notes.features.home_feature.event.HomeEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description


@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var getNotesUseCase: GetNotesUseCase
    private lateinit var notesEventListenUseCase: NotesEventListenUseCase
    private lateinit var repository: NotesRepository
    private lateinit var mockNotesDao: MockNotesDao
    private lateinit var notes: ArrayList<NoteEntity>

    private var testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        notes =
            arrayListOf(
                DummyNotesEntity.note1,
                DummyNotesEntity.note2,
                DummyNotesEntity.note3
            )
        mockNotesDao = MockNotesDao(notes)
        repository = NotesRepositoryImpl(mockNotesDao)
        getNotesUseCase = GetNotesUseCase(repository)
        notesEventListenUseCase = NotesEventListenUseCase(repository)
        homeViewModel = HomeViewModel(
            getNotesUseCase,
            notesEventListenUseCase,
            UnconfinedTestDispatcher(testDispatcher.scheduler),
            UnconfinedTestDispatcher(testDispatcher.scheduler)
        )
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getNotes_return3Notes() = runTest(testDispatcher.scheduler) {

        advanceUntilIdle()
        //Act when retrieve the note list
        val noteList = homeViewModel.notesList

        //Assert expect 3 notes in the list
        Truth.assertThat(noteList.size).isEqualTo(3)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getNotes_insertNewNote_returns4Notes() = runTest(testDispatcher.scheduler) {
        //Arrange setup home view model
        //Act when insert new note
        var note = DummyNotesEntity.note4.toModel()
        val noteId = repository.addNote(note)
        println("noteid=$noteId")
        advanceUntilIdle()
        val noteList = homeViewModel.notesList
        note = note.copy(id = noteId.toInt())
        val insertedNote = repository.getNoteById(noteId.toInt())
        advanceUntilIdle()

        //Assert note is inserted into note list and its size is 4
        Truth.assertThat(noteList.size).isEqualTo(4)
        Truth.assertThat(insertedNote).isEqualTo(note)

    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getNotes_updateNote_return3NotesAndUpdateNote() = runTest(testDispatcher.scheduler) {
        //Arrange setup home view model

        //Act when update a  note
        val note = DummyNotesEntity.note2.toModel().copy(title = "This is updated note")
        repository.updateNote(note)
        println("note=$note")
        advanceUntilIdle()
        val noteList = homeViewModel.notesList
        val updatedNote = noteList.first { it.id == note.id }
        //Assert that note is updated
        Truth.assertThat(updatedNote).isNotNull()
        Truth.assertThat(updatedNote).isEqualTo(note)
        Truth.assertThat(noteList.size).isEqualTo(3)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getNotes_deleteNote_return2Notes() = runTest(testDispatcher.scheduler) {
        //Arrange setup home view model

        //Act delete a note
        val isNoteDeleted = repository.deleteNote(DummyNotesEntity.note1.toModel())
        println("note=$isNoteDeleted")
        advanceUntilIdle()
        val noteList = homeViewModel.notesList
        val note = noteList.firstOrNull {
            it.id == DummyNotesEntity.note1.toModel().id
        }

        Truth.assertThat(note).isNull()
        Truth.assertThat(noteList.size).isEqualTo(2)

    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onNewItemClick_clickFistNote_emitHomeEventWithId() = runTest(testDispatcher.scheduler) {
        //Arrange setup home view model

        //Act
        val events = mutableListOf<HomeEvent>()
        val job = launch(testDispatcher.scheduler) {
            homeViewModel.event.toList(events)
        }
        advanceUntilIdle()
        homeViewModel.action(HomeAction.OnNoteItemClick(0))
        println("events ${events.size}")
        advanceUntilIdle()
        val noteList = homeViewModel.notesList
        Truth.assertThat(events.first())
            .isEqualTo(HomeEvent.OnItemClick(noteList[0].id))

        job.cancel()
    }

}