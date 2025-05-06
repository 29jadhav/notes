package com.vivek.notes.features.add_note_feature.presentation

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vivek.notes.R
import com.vivek.notes.features.add_note_feature.event.AddNoteAction
import com.vivek.notes.features.add_note_feature.event.AddNoteEvent
import com.vivek.notes.ui.components.AppAlertDialog
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddNoteScreen(
    navigateToBack: () -> Unit,
    addNoteViewModel: AddNoteViewModel = hiltViewModel()
) {
    val titleContentDescription = stringResource(R.string.add_new_title)
    val descriptionContentDescription = stringResource(R.string.add_new_description)
    val deleteBtn = stringResource(R.string.delete_btn)
    val backBtn= stringResource(R.string.back_btn)
    val appAlertDialog= stringResource(R.string.delete_alert_dialog)
    val addNotesScreen = stringResource(R.string.add_notes_screen)

    val showDeleteNoteDialog = remember { mutableStateOf(false) }
    val title = addNoteViewModel.title.collectAsState()
    val description = addNoteViewModel.description.collectAsState()

    LaunchedEffect(key1 = true) {
        addNoteViewModel.event.collectLatest { event ->
            when (event) {
                is AddNoteEvent.OnBackPress -> {
                    navigateToBack()
                }
            }
        }
    }
    AppAlertDialog(
        showDialog = showDeleteNoteDialog.value,
        onConfirmClick = {
            addNoteViewModel.action(AddNoteAction.OnDeleteNote)
            showDeleteNoteDialog.value = false
        }, onDismissClick = {
            showDeleteNoteDialog.value = false
        },
        modifier = Modifier.semantics { contentDescription=appAlertDialog })

    Scaffold(topBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth().semantics { contentDescription=addNotesScreen }
                .systemBarsPadding()
                .padding(
                    start = 16.dp, end = 16.dp, top = 8.dp,
                    bottom = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                Icons.AutoMirrored.Default.ArrowBack, contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        addNoteViewModel.onBackPress()
                    }.semantics { contentDescription = backBtn }
            )
            Icon(
                Icons.Default.Delete,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        showDeleteNoteDialog.value = true
                    }
                    .semantics { contentDescription = deleteBtn })
        }
    }) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
        ) {
            Column {
                TextField(
                    value = title.value, onValueChange = { title ->
                        addNoteViewModel.action(AddNoteAction.OnTitleChange(title))
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.enter_title), style =
                            MaterialTheme.typography.displaySmall
                        )
                    },
                    textStyle = MaterialTheme.typography.displaySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            contentDescription = titleContentDescription
                        }
                )

                TextField(
                    value = description.value, onValueChange = { description ->
                        addNoteViewModel.action(AddNoteAction.OnDescriptionChange(description))
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.enter_title), style =
                            MaterialTheme.typography.bodyLarge
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxSize()
                        .semantics {
                            contentDescription = descriptionContentDescription
                        }
                )
            }
        }
    }


}


@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun AddNoteScreenPreviewDarkMode() {
    AddNoteScreen(
        navigateToBack = {
            Log.d("TAG", "onBackPress....")
        },
    )
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
fun AddNoteScreenPreview() {
    AddNoteScreen(
        navigateToBack = {
            Log.d("TAG", "onBackPress....")
        },
    )
}