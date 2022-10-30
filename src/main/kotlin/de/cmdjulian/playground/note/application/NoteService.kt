package de.cmdjulian.playground.note.application

import de.cmdjulian.playground.note.domain.Note
import de.cmdjulian.playground.notebook.application.NotebookNotFoundException
import de.cmdjulian.playground.notebook.application.NotebookRepository
import de.cmdjulian.playground.notebook.application.findByIdOrThrow
import de.cmdjulian.playground.notebook.application.findByNotebookIdAndTagsAndContent
import de.cmdjulian.playground.tag.application.FindTagsUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class NoteService(private val repository: NotebookRepository, private val findTagsUseCase: FindTagsUseCase) {

    @Transactional
    fun createNote(note: Note, tagNames: List<String>): Note {
        val notebook = repository.findByIdOrThrow(note.id.notebookId)
        val tags = findTagsUseCase.findTagsByName(tagNames)
        note.tags = tags.toMutableSet()
        notebook.notes.add(note)

        return note
    }

    @Transactional(readOnly = true)
    fun findNotes(notebookId: UUID, tagNames: List<String>?, text: String?): List<Note> {
        if (!repository.existsById(notebookId)) throw NotebookNotFoundException(notebookId)
        val tags = tagNames?.let(findTagsUseCase::findTagsByName)

        return repository.findByNotebookIdAndTagsAndContent(notebookId, tags, text)
    }

    @Transactional(readOnly = true)
    fun findNote(notebookId: UUID, noteId: UUID): Note {
        val notebook = repository.findByIdOrThrow(notebookId)

        return notebook.notes.find { it.id.noteId == noteId } ?: throw NoteNotFoundException(notebookId, noteId)
    }

    @Transactional
    fun updateNote(notebookId: UUID, noteId: UUID, title: String, content: String?, tagNames: List<String>) {
        val tags = findTagsUseCase.findTagsByName(tagNames)

        findNote(notebookId, noteId).apply {
            this.title = title
            this.content = content
            this.tags = tags.toMutableSet()
        }
    }

    @Transactional
    fun deleteNote(notebookId: UUID, noteId: UUID) {
        val notebook = repository.findByIdOrThrow(notebookId)
        val noteRemoved = notebook.notes.removeIf { it.id.noteId == noteId }

        if (!noteRemoved) {
            throw NoteNotFoundException(notebookId, noteId)
        }
    }
}
