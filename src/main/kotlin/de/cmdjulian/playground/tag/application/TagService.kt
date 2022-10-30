package de.cmdjulian.playground.tag.application

import de.cmdjulian.playground.tag.domain.Color
import de.cmdjulian.playground.tag.domain.Tag
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TagService(private val repository: TagRepository) : FindTagsUseCase {

    @Transactional
    fun createTag(tag: Tag): Tag {
        if (repository.existsByNameIgnoreCase(tag.name)) {
            throw TagAlreadyExistsException(tag.name)
        }

        return repository.save(tag)
    }

    @Transactional(readOnly = true)
    fun findTags(): List<Tag> = repository.findAll()

    @Transactional(readOnly = true)
    fun findTag(name: String): Tag {
        return repository.findByNameIgnoreCase(name) ?: throw TagNotFoundException(name)
    }

    @Transactional
    fun updateTag(name: String, color: Color) {
        findTag(name).apply { this.color = color }
    }

    @Transactional
    fun deleteTag(name: String) {
        findTag(name).let(repository::delete)
    }

    @Transactional(readOnly = true)
    override fun findTagsByName(names: List<String>?): List<Tag> {
        if (names.isNullOrEmpty()) {
            return emptyList()
        }

        return repository.findAllById(names).also { tags ->
            if (tags.size != names.size) {
                throw TagNotFoundException(names.first { name -> name !in tags.map(Tag::name) })
            }
        }
    }
}
