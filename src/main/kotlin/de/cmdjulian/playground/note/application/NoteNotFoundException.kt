package de.cmdjulian.playground.note.application

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

@ResponseStatus(HttpStatus.NOT_FOUND)
class NoteNotFoundException(notebookId: UUID, noteId: UUID) : RuntimeException(
    "Note with id $noteId not found in notebook with id $notebookId"
)
