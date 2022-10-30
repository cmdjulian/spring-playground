package de.cmdjulian.playground.notebook.application

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

@ResponseStatus(code = HttpStatus.NOT_FOUND)
class NotebookNotFoundException(id: UUID) : RuntimeException("Notebook with id '$id' not found")
