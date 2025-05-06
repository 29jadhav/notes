package com.vivek.notes.features.add_note_feature.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivek.notes.core.di.IODispatcher
import com.vivek.notes.features.add_note_feature.domain.AddNoteUseCase
import com.vivek.notes.features.add_note_feature.domain.DeleteNoteUseCase
import com.vivek.notes.features.add_note_feature.domain.GetNoteUseCase
import com.vivek.notes.features.add_note_feature.event.AddNoteEvent
import com.vivek.notes.core.domain.model.Note
import com.vivek.notes.features.add_note_feature.event.AddNoteAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val addNoteFeature: AddNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val getNoteUseCase: GetNoteUseCase,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {


    private var noteId: Int
    private var _title: MutableStateFlow<String> = MutableStateFlow("")
    val title = _title.asStateFlow()

    private var _description: MutableStateFlow<String> = MutableStateFlow("")
    val description = _description.asStateFlow()

    private var _event = MutableSharedFlow<AddNoteEvent>()
    val event = _event.asSharedFlow()

    init {
        val id = savedStateHandle.get<Int>("id") ?: -1
        noteId = id
        retrieveNote(id)
    }

    private fun retrieveNote(id: Int) {
        viewModelScope.launch(ioDispatcher) {
            if (id != -1) {
                val note = getNoteUseCase.execute(noteId) ?: return@launch
                _title.value = note.title
                _description.value = note.description
            }
        }
    }


    fun action(action: AddNoteAction) {
        when (action) {
            AddNoteAction.OnDeleteNote -> deleteNote()
            is AddNoteAction.OnDescriptionChange -> onDescriptionChange(action.description)
            is AddNoteAction.OnTitleChange -> onTitleChange(action.title)
        }
    }

    private fun onTitleChange(title: String) {
        _title.value = title
    }

    private fun onDescriptionChange(description: String) {
        _description.value = description
    }


    fun onBackPress() {
        val note = Note(id = noteId, title = _title.value, description = _description.value)
        viewModelScope.launch(ioDispatcher) {
            addNoteFeature.execute(note)
            _event.emit(AddNoteEvent.OnBackPress)
        }
    }

    private fun deleteNote() {
        viewModelScope.launch(ioDispatcher) {
            deleteNoteUseCase.execute(noteId)
            _event.emit(AddNoteEvent.OnBackPress)
        }
    }

}