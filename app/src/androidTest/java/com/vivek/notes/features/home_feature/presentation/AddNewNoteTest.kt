package com.vivek.notes.features.home_feature.presentation

import android.content.Context
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import com.vivek.notes.R
import com.vivek.notes.SetNavigation
import com.vivek.notes.core.data.local.DummyNotesEntity
import com.vivek.notes.core.data.local.entity.toModel
import org.junit.Rule
import org.junit.Test

class AddNewNoteTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun addANote_noteShouldBeVisibleOnHomeScreen() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        val fabButton = context.getString(R.string.add_new_note)
        val addNotesScreen = context.getString(R.string.add_notes_screen)
        val title = context.getString(R.string.add_new_note)
        val description = context.getString(R.string.add_new_description)
        val backBtn = context.getString(R.string.back_btn)
        val homeScreenTitle = context.getString(R.string.notes_title)

        composeTestRule.setContent {
            SetNavigation()
        }
        composeTestRule.onNodeWithContentDescription(fabButton).performClick()
        composeTestRule.waitUntil(1000L) {
            composeTestRule.onNodeWithContentDescription(addNotesScreen).isDisplayed()
        }
        val note = DummyNotesEntity.note1.toModel()
        composeTestRule.onNodeWithContentDescription(title).performTextInput(note.title)
        composeTestRule.onNodeWithContentDescription(description).performTextInput(note.description)
        composeTestRule.onNodeWithContentDescription(backBtn).performClick()

        composeTestRule.waitUntil(1000L) {
            composeTestRule.onNodeWithText(homeScreenTitle).isDisplayed()
        }

        composeTestRule.onNodeWithText(note.title).isDisplayed()
        composeTestRule.onNodeWithText(note.description).isDisplayed()

    }
}