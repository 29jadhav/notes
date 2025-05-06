package com.vivek.notes.core.di

import android.content.Context
import androidx.room.Room
import com.vivek.notes.core.data.local.AppDatabase
import com.vivek.notes.core.data.repository.NotesRepositoryImpl
import com.vivek.notes.core.domain.repository.NotesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesNotesRepository(appDatabase: AppDatabase): NotesRepository {
        return NotesRepositoryImpl(appDatabase.notesDao())
    }

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context = context,
            AppDatabase::class.java,
            "notes_db"
        ).build()
    }

    @IODispatcher
    @Provides
    @Singleton
    fun providesIODispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @MainDispatcher
    @Provides
    @Singleton
    fun providesMainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }
}