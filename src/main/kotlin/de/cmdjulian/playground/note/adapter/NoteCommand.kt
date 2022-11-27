package de.cmdjulian.playground.note.adapter

import jakarta.validation.constraints.Size

data class NoteCommand(
    @get:Size(min = 1, max = 128) val title: String,
    @get:Size(max = 65536) val content: String?,
    val tags: List<String> = emptyList()
)
