package de.cmdjulian.playground.note.adapter

import de.cmdjulian.playground.tag.adapter.TagModel
import java.util.*

data class NoteDto(val id: UUID, val title: String, val content: String?, val tags: List<TagModel>)
