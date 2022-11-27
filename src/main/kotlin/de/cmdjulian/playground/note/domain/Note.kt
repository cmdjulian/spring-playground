package de.cmdjulian.playground.note.domain

import de.cmdjulian.playground.tag.domain.Tag
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import org.hibernate.Hibernate
import org.hibernate.annotations.DynamicUpdate
import java.io.Serializable
import java.util.*

@Entity
@DynamicUpdate
@Table(name = "note")
class Note(
    notebookId: UUID,
    @Column(length = 128, nullable = false) var title: String,
    @Column(length = 65536) var content: String?
) {

    @EmbeddedId
    val id = NoteId(notebookId, UUID.randomUUID())

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "note_tag",
        joinColumns = [JoinColumn(name = "note_id"), JoinColumn(name = "notebook_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    var tags: MutableSet<Tag> = mutableSetOf()
        set(value) {
            field.clear()
            field.addAll(value)
        }

    override fun equals(other: Any?) = when {
        this === other -> true
        other == null || Hibernate.getClass(this) != Hibernate.getClass(other) -> false
        else -> other is Note && id == other.id
    }

    override fun hashCode() = id.hashCode()

    override fun toString() =
        "Note(notebookId=${id.notebookId}, title='$title', content='$content', nodeId=${id.noteId})"
}

@Embeddable
data class NoteId(
    @Column(columnDefinition = "uuid") val notebookId: UUID,
    @Column(columnDefinition = "uuid") val noteId: UUID
) : Serializable {

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other !is NoteId -> false
            notebookId != other.notebookId -> false
            noteId != other.noteId -> false
            else -> true
        }
    }

    override fun hashCode(): Int = 31 * notebookId.hashCode() + noteId.hashCode()
}
