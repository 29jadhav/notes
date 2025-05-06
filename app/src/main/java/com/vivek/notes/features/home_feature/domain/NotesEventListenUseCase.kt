package com.vivek.notes.features.home_feature.domain

import com.vivek.notes.core.domain.repository.NotesRepository
import com.vivek.notes.features.home_feature.event.NotesEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesEventListenUseCase(private val notesRepository: NotesRepository) {

    suspend fun execute(): Flow<NotesEvent> {
        return channelFlow {
            withContext(Dispatchers.IO) {
                launch {
                    notesRepository.addNoteListener.collect { newNote ->
                        send(NotesEvent.NewNote(newNote))
                    }
                }

                launch {
                    notesRepository.deleteNoteListener.collect { deletedNote ->
                        send((NotesEvent.DeleteNote(deletedNote)))
                    }
                }

                launch {
                    notesRepository.updateNoteListener.collect { updatedNote ->
                        send(NotesEvent.UpdateNote(updatedNote))
                    }
                }
            }
        }
    }
}