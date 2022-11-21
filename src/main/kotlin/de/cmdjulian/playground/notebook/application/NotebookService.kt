package de.cmdjulian.playground.notebook.application

import de.cmdjulian.playground.notebook.domain.Notebook
import de.cmdjulian.playground.tag.application.FindTagsUseCase
import de.cmdjulian.playground.tag.domain.Tag
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class NotebookService(
    private val repository: NotebookRepository,
    private val findTagsUseCase: FindTagsUseCase
) {

    @Transactional
    fun createNotebook(notebook: Notebook, tagNames: List<String>): Notebook {
        val tags = findTagsUseCase.findTagsByName(tagNames)

        return notebook.apply { this.tags = tags.toMutableSet() }
            .also { repository.save(it) }
    }

    @Transactional(readOnly = true)
    fun findAllNotebooks(specification: Specification<Notebook>): List<Notebook> {
        return repository.findAll(specification)
    }

    @Transactional(readOnly = true)
    fun findAllNotebooks(tagNames: List<String>?, text: String?): List<Notebook> {
        val spec = Specification { root: Root<Notebook>, _, builder ->
            val predicates: MutableList<Predicate> = mutableListOf()

            if (tagNames?.isNotEmpty() == true) {
                val join = root.join<Notebook, Tag>(Notebook::tags.name)
                val tagPredicates = join.get<String>(Tag::name.name).`in`(tagNames)

                predicates.add(tagPredicates)
            }

            if (text != null) {
                val titlePredicate = builder.like(root.get(Notebook::title.name), "%$text%")
                val descriptionPredicate = builder.like(root.get(Notebook::description.name), "%$text%")
                val textPredicate = builder.or(titlePredicate, descriptionPredicate)

                predicates.add(textPredicate)
            }

            return@Specification when (predicates.size) {
                0 -> null
                1 -> predicates.first()
                else -> builder.and(*predicates.toTypedArray())
            }
        }

        return repository.findAll(spec)
    }

    @Transactional(readOnly = true)
    fun findNotebook(id: UUID): Notebook = repository.findByIdOrThrow(id)

    @Transactional
    fun updateNotebook(id: UUID, title: String, description: String?, tagNames: List<String>) {
        val tags = findTagsUseCase.findTagsByName(tagNames)

        repository.findByIdOrThrow(id).apply {
            this.title = title
            this.description = description
            this.tags = tags.toMutableSet()
        }
    }

    @Transactional
    fun deleteNotebook(id: UUID) {
        repository.findByIdOrThrow(id).let(repository::delete)
    }
}
