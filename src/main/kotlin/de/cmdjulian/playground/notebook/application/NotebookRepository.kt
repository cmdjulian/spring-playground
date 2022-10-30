package de.cmdjulian.playground.notebook.application

import de.cmdjulian.playground.note.domain.Note
import de.cmdjulian.playground.notebook.domain.Notebook
import de.cmdjulian.playground.tag.domain.Tag
import jakarta.persistence.criteria.JoinType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface NotebookRepository : JpaRepository<Notebook, UUID>, JpaSpecificationExecutor<Notebook>

fun NotebookRepository.findByIdOrThrow(id: UUID): Notebook = findByIdOrNull(id) ?: throw NotebookNotFoundException(id)

fun NotebookRepository.findByNotebookIdAndTagsAndContent(id: UUID, tags: List<Tag>?, text: String?): List<Note> =
    this.findOne { root, _, builder ->
        val predicates = mutableListOf(builder.equal(root.get<UUID>(Notebook::id.name), id))
        val note = root.join<Notebook, Note>(Notebook::notes.name, JoinType.INNER)

        if (tags?.isNotEmpty() == true) {
            val tagsPredicate = note.join<Note, Tag>(Note::tags.name, JoinType.INNER).`in`(tags)
            predicates.add(tagsPredicate)
        }

        if (text != null) {
            val titlePredicate = builder.like(note.get(Note::title.name), "%$text%")
            val contentPredicate = builder.like(note.get(Note::content.name), "%$text%")

            predicates.add(builder.or(titlePredicate, contentPredicate))
        }

        return@findOne builder.and(*predicates.toTypedArray())
    }.map { notebook -> notebook.notes.toList() }.orElseGet(::emptyList)
