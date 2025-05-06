package com.vivek.notes.features.add_note_feature.presentation

import android.content.Context
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.SavedStateHandle
import androidx.test.core.app.ApplicationProvider
import com.vivek.notes.R
import com.vivek.notes.core.data.local.DummyNotesEntity
import com.vivek.notes.core.data.local.MockNotesDao
import com.vivek.notes.core.data.local.NotesDao
import com.vivek.notes.core.data.local.entity.NoteEntity
import com.vivek.notes.core.data.repository.NotesRepositoryImpl
import com.vivek.notes.core.domain.repository.NotesRepository
import com.vivek.notes.features.add_note_feature.domain.AddNoteUseCase
import com.vivek.notes.features.add_note_feature.domain.DeleteNoteUseCase
import com.vivek.notes.features.add_note_feature.domain.GetNoteUseCase
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class AddNoteScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var addNoteViewModel: AddNoteViewModel
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var addNoteUseCase: AddNoteUseCase
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase
    private lateinit var getNoteUseCase: GetNoteUseCase
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
        savedStateHandle = SavedStateHandle()
        notesDao = MockNotesDao(notes)
        notesRepository = NotesRepositoryImpl(notesDao)
        addNoteUseCase = AddNoteUseCase(notesRepository)
        deleteNoteUseCase = DeleteNoteUseCase(notesRepository)
        getNoteUseCase = GetNoteUseCase(notesRepository)

        addNoteViewModel = AddNoteViewModel(
            savedStateHandle,
            addNoteUseCase,
            deleteNoteUseCase,
            getNoteUseCase,
            Dispatchers.IO
        )
    }


    @Test
    fun addNoteScreen_addTitleAndDescription_titleAndDescriptionShouldVisible() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val titleContentDescription = context.getString(R.string.add_new_title)
        val descriptionContentDescription = context.getString(R.string.add_new_description)
        composeTestRule.setContent {
            AddNoteScreen(navigateToBack = {}, addNoteViewModel)
        }
        val title = "Title of the note"
        composeTestRule.onNodeWithContentDescription(titleContentDescription)
            .performTextInput(title)
        composeTestRule.onNodeWithText(title).assertIsDisplayed()

        val description = "This is note description"
        composeTestRule.onNodeWithContentDescription(descriptionContentDescription)
            .performTextInput(description)
        composeTestRule.onNodeWithText(description).assertIsDisplayed()
    }


    @Test
    fun addNoteScreen_onClickOfDeleteButtonDialogShouldShow() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val deleteBtn = context.getString(R.string.delete_btn)
        val deleteDialog = context.getString(R.string.delete_alert_dialog)
        val yes = context.getString(R.string.yes)
        val no = context.getString(R.string.no)
        composeTestRule.setContent {
            AddNoteScreen(navigateToBack = {}, addNoteViewModel)
        }
        composeTestRule.onNodeWithContentDescription(deleteBtn).performClick()
        composeTestRule.mainClock.advanceTimeBy(1000L)
        composeTestRule.onNodeWithContentDescription(deleteDialog).assertIsDisplayed()
        composeTestRule.onNodeWithText(no).performClick()
        composeTestRule.onNodeWithContentDescription(deleteDialog).assertIsNotDisplayed()
        composeTestRule.onNodeWithContentDescription(deleteBtn).performClick()
        composeTestRule.mainClock.advanceTimeBy(1000L)
        composeTestRule.onNodeWithText(yes).performClick()
        composeTestRule.onNodeWithContentDescription(deleteDialog).assertIsNotDisplayed()

    }
}