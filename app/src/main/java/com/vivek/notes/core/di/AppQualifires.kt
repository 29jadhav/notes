package com.vivek.notes.core.di

import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IODispatcher


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher

