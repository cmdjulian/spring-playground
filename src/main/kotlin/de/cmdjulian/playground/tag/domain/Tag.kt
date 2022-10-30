package de.cmdjulian.playground.tag.domain

import de.cmdjulian.playground.note.domain.Note
import de.cmdjulian.playground.notebook.domain.Notebook
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.PreRemove
import jakarta.persistence.Table
import org.hibernate.Hibernate

@Entity
@Table(name = "tag")
class Tag(@Id val name: String, var color: Color) {

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private val notebooks: Set<Notebook> = emptySet()

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private val notes: Set<Note> = emptySet()

    @PreRemove
    fun preRemove() {
        notebooks.forEach { notebook -> notebook.tags.remove(this) }
        notes.forEach { note -> note.tags.remove(this) }
    }

    override fun equals(other: Any?): Boolean = when {
        this === other -> true
        other == null || Hibernate.getClass(this) != Hibernate.getClass(other) -> false
        else -> other is Tag && name != other.name
    }

    override fun hashCode(): Int = name.hashCode()
}

enum class Color {
    RED, GREEN, BLUE
}
