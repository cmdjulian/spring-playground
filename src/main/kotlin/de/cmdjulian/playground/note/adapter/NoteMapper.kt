package de.cmdjulian.playground.note.adapter

import de.cmdjulian.playground.note.domain.Note
import de.cmdjulian.playground.tag.adapter.toModel
import java.util.*

fun NoteCommand.toNote(notebookId: UUID): Note = Note(notebookId, title, content)

fun Note.toDto(): NoteDto = NoteDto(id.noteId, title, content, tags.map { tag -> tag.toModel() })
