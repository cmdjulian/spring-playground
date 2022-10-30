package de.cmdjulian.playground.note.adapter

import de.cmdjulian.playground.note.application.NoteService
import de.cmdjulian.playground.note.domain.Note
import de.cmdjulian.playground.shared.NOTE_ENDPOINT
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(NOTE_ENDPOINT)
class NoteController(private val service: NoteService) {

    @PostMapping
    fun createNote(@PathVariable notebookId: UUID, @Valid @RequestBody command: NoteCommand): NoteDto {
        return service.createNote(command.toNote(notebookId), command.tags).toDto()
    }

    @GetMapping
    fun findNotes(@PathVariable notebookId: UUID, filters: NoteFilters): List<NoteDto> {
        return service.findNotes(notebookId, filters.tagNames, filters.content).map(Note::toDto)
    }

    @GetMapping("/{noteId}")
    fun findNote(@PathVariable notebookId: UUID, @PathVariable noteId: UUID): NoteDto {
        return service.findNote(noteId, notebookId).toDto()
    }

    @PutMapping("/{noteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateNote(
        @PathVariable notebookId: UUID,
        @PathVariable noteId: UUID,
        @Valid @RequestBody
        command: NoteCommand
    ) {
        service.updateNote(notebookId, noteId, command.title, command.content, command.tags)
    }

    @DeleteMapping("/{noteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteNote(@PathVariable notebookId: UUID, @PathVariable noteId: UUID) {
        service.deleteNote(notebookId, noteId)
    }
}

data class NoteFilters(val tagNames: List<String>?, val content: String?)
