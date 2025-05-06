package com.vivek.notes.features.home_feature.event

sealed class HomeEvent {
    data class OnItemClick(val id: Int) : HomeEvent()
}

sealed class HomeAction {
    data class OnNoteItemClick(val index: Int) : HomeAction()
}