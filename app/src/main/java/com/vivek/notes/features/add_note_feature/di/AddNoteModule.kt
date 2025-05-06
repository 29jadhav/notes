package com.vivek.notes.features.add_note_feature.di

import com.vivek.notes.core.domain.repository.NotesRepository
import com.vivek.notes.features.add_note_feature.domain.AddNoteUseCase
import com.vivek.notes.features.add_note_feature.domain.DeleteNoteUseCase
import com.vivek.notes.features.add_note_feature.domain.GetNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object AddNoteModule {

    @Provides
    fun providesAddNoteUseCase(repository: NotesRepository): AddNoteUseCase{
        return AddNoteUseCase(repository)
    }

    @Provides
    fun providesDeleteNoteUseCase(repository: NotesRepository): DeleteNoteUseCase{
        return DeleteNoteUseCase(repository)
    }


    @Provides
    fun providesGetNoteUseCase(repository: NotesRepository): GetNoteUseCase{
        return GetNoteUseCase(repository)
    }
}