package com.vivek.notes.features.home_feature.di

import com.vivek.notes.core.domain.repository.NotesRepository
import com.vivek.notes.features.home_feature.domain.GetNotesUseCase
import com.vivek.notes.features.home_feature.domain.NotesEventListenUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object HomeModule {

    @Provides
    fun providesGetNotesUseCase(repository: NotesRepository): GetNotesUseCase {
        return GetNotesUseCase(repository)
    }

    @Provides
    fun provideNotesEventListenUseCase(repository: NotesRepository): NotesEventListenUseCase{
        return NotesEventListenUseCase(repository)
    }
}