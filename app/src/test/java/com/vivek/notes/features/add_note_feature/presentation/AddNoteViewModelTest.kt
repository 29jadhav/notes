package com.vivek.notes.features.add_note_feature.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth
import com.vivek.notes.DummyNotesEntity
import com.vivek.notes.core.data.local.MockNotesDao
import com.vivek.notes.core.data.local.entity.NoteEntity
import com.vivek.notes.core.data.local.entity.toModel
import com.vivek.notes.core.data.repository.NotesRepositoryImpl
import com.vivek.notes.core.domain.model.Note
import com.vivek.notes.core.domain.repository.NotesRepository
import com.vivek.notes.features.add_note_feature.domain.AddNoteUseCase
import com.vivek.notes.features.add_note_feature.domain.DeleteNoteUseCase
import com.vivek.notes.features.add_note_feature.domain.GetNoteUseCase
import com.vivek.notes.features.add_note_feature.event.AddNoteAction
import com.vivek.notes.features.home_feature.presentation.MainDispatcherRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

class AddNoteViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var addNoteViewModel: AddNoteViewModel
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var addNoteUseCase: AddNoteUseCase
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase
    private lateinit var getNoteUseCase: GetNoteUseCase
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
        savedStateHandle = SavedStateHandle()
        mockNotesDao = MockNotesDao(notes)
        repository = NotesRepositoryImpl(mockNotesDao)
        deleteNoteUseCase = DeleteNoteUseCase(repository)
        getNoteUseCase = GetNoteUseCase(repository)
        addNoteUseCase = AddNoteUseCase(repository)
        addNoteViewModel =
            AddNoteViewModel(
                savedStateHandle,
                addNoteUseCase,
                deleteNoteUseCase,
                getNoteUseCase,
                UnconfinedTestDispatcher(testDispatcher.scheduler)
            )
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getNote_noteId_getTitleAndDescription() = runTest(testDispatcher.scheduler) {

        //Arrange setup note view model
        val note = DummyNotesEntity.note1.toModel()
        savedStateHandle["id"] = note.id
        addNoteViewModel =
            AddNoteViewModel(
                savedStateHandle,
                addNoteUseCase,
                deleteNoteUseCase,
                getNoteUseCase,
                UnconfinedTestDispatcher(testDispatcher.scheduler)
            )

        //Act retrieve title and description
        val title = addNoteViewModel.title.value
        val description = addNoteViewModel.description.value

        //Assert
        Truth.assertThat(title).isEqualTo(note.title)
        Truth.assertThat(description).isEqualTo(note.description)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getTitle_updatedTitleAction_returnsUpdatedTitle() = runTest(testDispatcher.scheduler) {

        //Arrange setup note view model
        val note = DummyNotesEntity.note1.toModel()
        savedStateHandle["id"] = note.id
        addNoteViewModel =
            AddNoteViewModel(
                savedStateHandle,
                addNoteUseCase,
                deleteNoteUseCase,
                getNoteUseCase,
                UnconfinedTestDispatcher(testDispatcher.scheduler)
            )


        addNoteViewModel.action(AddNoteAction.OnTitleChange("This is updated title"))
        advanceUntilIdle()
        //Assert
        Truth.assertThat(addNoteViewModel.title.value).isEqualTo("This is updated title")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getTitle_updatedDescriptionAction_returnsUpdateDescription() = runTest(testDispatcher
        .scheduler) {

        //Arrange setup note view model
        val note = DummyNotesEntity.note1.toModel()
        savedStateHandle["id"] = note.id
        addNoteViewModel =
            AddNoteViewModel(
                savedStateHandle,
                addNoteUseCase,
                deleteNoteUseCase,
                getNoteUseCase,
                UnconfinedTestDispatcher(testDispatcher.scheduler)
            )


        addNoteViewModel.action(AddNoteAction.OnDescriptionChange("This is updated description"))
        advanceUntilIdle()
        //Assert
        Truth.assertThat(addNoteViewModel.description.value).isEqualTo("This is updated description")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteNote_noteId_returnsNull() = runTest(testDispatcher
        .scheduler) {

        //Arrange setup note view model
        val note = DummyNotesEntity.note1.toModel()
        savedStateHandle["id"] = note.id
        addNoteViewModel =
            AddNoteViewModel(
                savedStateHandle,
                addNoteUseCase,
                deleteNoteUseCase,
                getNoteUseCase,
                UnconfinedTestDispatcher(testDispatcher.scheduler)
            )


        addNoteViewModel.action(AddNoteAction.OnDeleteNote)
        advanceUntilIdle()
        val deletedNote = repository.getNoteById(note.id)
        advanceUntilIdle()
        //Assert
        Truth.assertThat(deletedNote).isNull()
    }
}