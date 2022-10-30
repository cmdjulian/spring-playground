package de.cmdjulian.playground.notebook.adapter

import de.cmdjulian.playground.note.adapter.toDto
import de.cmdjulian.playground.note.domain.Note
import de.cmdjulian.playground.notebook.domain.Notebook
import de.cmdjulian.playground.tag.adapter.toModel

fun NotebookCommand.toNotebook() = Notebook(title, description)

fun Notebook.toDto() = NotebookDto(id, title, description, notes.map(Note::toDto), tags.map { tag -> tag.toModel() })
