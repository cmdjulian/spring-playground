package de.cmdjulian.playground.tag.application

import de.cmdjulian.playground.tag.domain.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : JpaRepository<Tag, String> {

    fun findByNameIgnoreCase(name: String): Tag?

    fun existsByNameIgnoreCase(name: String): Boolean
}
