package de.cmdjulian.playground.notebook.adapter

import de.cmdjulian.playground.note.adapter.NoteDto
import de.cmdjulian.playground.tag.adapter.TagModel
import java.util.*

data class NotebookDto(
    val id: UUID,
    val title: String,
    val description: String?,
    val notes: List<NoteDto>,
    val tags: List<TagModel>
)
