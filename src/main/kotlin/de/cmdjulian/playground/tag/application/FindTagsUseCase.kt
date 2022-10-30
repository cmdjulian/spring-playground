package de.cmdjulian.playground.tag.application

import de.cmdjulian.playground.tag.domain.Tag

interface FindTagsUseCase {
    fun findTagsByName(names: List<String>?): List<Tag>
}
