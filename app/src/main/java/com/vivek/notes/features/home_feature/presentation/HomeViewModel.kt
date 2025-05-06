package com.vivek.notes.features.home_feature.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivek.notes.core.di.IODispatcher
import com.vivek.notes.core.di.MainDispatcher
import com.vivek.notes.core.domain.model.Note
import com.vivek.notes.features.home_feature.domain.GetNotesUseCase
import com.vivek.notes.features.home_feature.domain.NotesEventListenUseCase
import com.vivek.notes.features.home_feature.event.HomeAction
import com.vivek.notes.features.home_feature.event.HomeEvent
import com.vivek.notes.features.home_feature.event.NotesEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val notesEventListenUseCase: NotesEventListenUseCase,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _event = MutableSharedFlow<HomeEvent>()
    val event = _event.asSharedFlow()


    val notesList = mutableStateListOf<Note>()
    private val _scope = viewModelScope

    init {
        retrieveNotes(ioDispatcher)
        listenToNotesEvents(ioDispatcher)
    }

    private fun listenToNotesEvents(coroutineDispatcher: CoroutineDispatcher) {
        _scope.launch(coroutineDispatcher) {
            notesEventListenUseCase.execute().collect { notesEvent ->
                when (notesEvent) {
                    is NotesEvent.DeleteNote -> {
                        notesList.remove(notesEvent.note)
                    }

                    is NotesEvent.NewNote -> {
                        println("received new ote event")
                        notesList.add(notesEvent.note)
                    }

                    is NotesEvent.UpdateNote -> {
                        val index =
                            notesList.indexOfFirst { noteItem -> noteItem.id == notesEvent.note.id }
                        if (index != -1)
                            notesList[index] = notesEvent.note
                    }
                }

            }
        }
    }

    private fun retrieveNotes(coroutineDispatcher: CoroutineDispatcher) {
        _scope.launch(coroutineDispatcher) {
            val notes = getNotesUseCase.execute()
            delay(500L)
            notesList.addAll(notes)
        }
    }

    fun action(action: HomeAction) {
        when (action) {
            is HomeAction.OnNoteItemClick -> onNoteItemClick(action.index)
        }
    }

    private fun onNoteItemClick(index: Int) {
//        Log.d(TAG, "You clicked note ${notesList[index]}")
        viewModelScope.launch(mainDispatcher) {
            _event.emit(HomeEvent.OnItemClick(notesList[index].id))
        }
    }


    companion object {
        private const val TAG = "HomeViewModel"
    }
}