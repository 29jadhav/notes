package com.vivek.notes.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vivek.notes.core.domain.model.Note

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val title: String,
    val description: String
)

fun NoteEntity.toModel(): Note {
    return Note(
        id = id ?: -1,
        title = this.title,
        description = this.description
    )
}

