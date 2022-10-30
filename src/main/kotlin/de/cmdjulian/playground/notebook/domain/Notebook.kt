package de.cmdjulian.playground.notebook.domain

import de.cmdjulian.playground.note.domain.Note
import de.cmdjulian.playground.tag.domain.Tag
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.Hibernate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "notebook")
@EntityListeners(AuditingEntityListener::class)
class Notebook(
    @Column(length = 128, nullable = false) var title: String,
    @Column(length = 1024) var description: String?
) {

    @Id
    @Column(columnDefinition = "uuid")
    var id: UUID = UUID.randomUUID()

    @Column(updatable = false, nullable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now()

    @LastModifiedDate
    @Column(nullable = false)
    lateinit var lastModified: OffsetDateTime

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "id.notebookId", cascade = [CascadeType.ALL], orphanRemoval = true)
    var notes: MutableSet<Note> = mutableSetOf()
        set(value) {
            field.clear()
            field.addAll(value)
        }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "notebook_tag",
        joinColumns = [JoinColumn(name = "notebook_id")],
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
        else -> other is Notebook && id == other.id
    }

    override fun hashCode() = id.hashCode()

    override fun toString() = "Notebook(id=$id, title='$title', description=$description, createdAt=$createdAt, " +
        "lastModified=$lastModified, notes=${notes.size})"
}
