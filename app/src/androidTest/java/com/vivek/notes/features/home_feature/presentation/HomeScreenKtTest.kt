package com.vivek.notes.features.home_feature.presentation

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import com.vivek.notes.R
import com.vivek.notes.core.data.local.DummyNotesEntity
import com.vivek.notes.core.data.local.MockNotesDao
import com.vivek.notes.core.data.local.NotesDao
import com.vivek.notes.core.data.local.entity.NoteEntity
import com.vivek.notes.core.data.local.entity.toModel
import com.vivek.notes.core.data.repository.NotesRepositoryImpl
import com.vivek.notes.core.domain.repository.NotesRepository
import com.vivek.notes.features.home_feature.domain.GetNotesUseCase
import com.vivek.notes.features.home_feature.domain.NotesEventListenUseCase
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var getNotesUseCase: GetNotesUseCase
    private lateinit var notesEventListenUseCase: NotesEventListenUseCase
    private lateinit var notesRepository: NotesRepository
    private lateinit var notesDao: NotesDao
    private lateinit var notes: ArrayList<NoteEntity>

    @Before
    fun setup() {
        notes =
            arrayListOf(
                DummyNotesEntity.note1,
                DummyNotesEntity.note2,
                DummyNotesEntity.note3
            )
        notesDao = MockNotesDao(notes)
        notesRepository = NotesRepositoryImpl(notesDao)
        getNotesUseCase = GetNotesUseCase(notesRepository)
        notesEventListenUseCase = NotesEventListenUseCase(notesRepository)
        homeViewModel = HomeViewModel(
            getNotesUseCase = getNotesUseCase,
            notesEventListenUseCase = notesEventListenUseCase,
            ioDispatcher = Dispatchers.IO,
            mainDispatcher = Dispatchers.Main
        )
    }

    @Test
    fun homeScreen_verifyScreenTitleAndFabButton() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val expectedTitle = context.getString(R.string.notes_title)
        composeTestRule.setContent {
            HomeScreen(onAddNoteClick = {}, homeViewModel)
        }
        composeTestRule
            .onNodeWithText(expectedTitle)
            .assertIsDisplayed()

    }

    @Test
    fun homeScreen_getNotes_verifyThreeItemsInList() {
        composeTestRule.setContent {
            HomeScreen(onAddNoteClick = {}, homeViewModel)
        }
        val note1 = DummyNotesEntity.note1.toModel()
        val note2 = DummyNotesEntity.note2.toModel()
        val note3 = DummyNotesEntity.note3.toModel()

        note1.let { note ->
            composeTestRule.onNodeWithText(note.title).assertIsDisplayed()
            composeTestRule.onNodeWithText(note.description).assertIsDisplayed()
        }
        note2.let { note ->
            composeTestRule.onNodeWithText(note.title).assertIsDisplayed()
            composeTestRule.onNodeWithText(note.description).assertIsDisplayed()
        }
        note3.let { note ->
            composeTestRule.onNodeWithText(note.title).assertIsDisplayed()
            composeTestRule.onNodeWithText(note.description).assertIsDisplayed()
        }

    }

    @Test
    fun homeScreen_onFABButtonClick_verifyNavigateAddNoteScreen() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val addNote = context.getString(R.string.add_new_note)
        var navigateNextRoute = 0
        composeTestRule.setContent {
            HomeScreen(onAddNoteClick = {
                navigateNextRoute = it
            }, homeViewModel)
        }

        composeTestRule.onNodeWithContentDescription(addNote).performClick()

        Truth.assertThat(navigateNextRoute).isEqualTo(-1)
    }

    @Test
    fun homeScreen_onNoteItemClick_verifyClickedItemIsSelected() {
        //Arrange
        val context = ApplicationProvider.getApplicationContext<Context>()
        var navigateNextRoute = 0
        composeTestRule.setContent {
            HomeScreen(onAddNoteClick = {
                navigateNextRoute = it
            }, homeViewModel)
        }

        //Act
        val note1 = DummyNotesEntity.note1.toModel()
        composeTestRule.onNodeWithText(note1.title).performClick()
        composeTestRule.mainClock.advanceTimeBy(1000L)
        //Assert
        Truth.assertThat(navigateNextRoute).isEqualTo(note1.id)

    }
}