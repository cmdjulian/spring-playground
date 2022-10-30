@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package de.cmdjulian.playground.config

import de.cmdjulian.playground.note.adapter.NoteCommand
import de.cmdjulian.playground.note.adapter.NoteFilters
import de.cmdjulian.playground.note.domain.NoteId
import de.cmdjulian.playground.notebook.adapter.NotebookCommand
import de.cmdjulian.playground.notebook.adapter.NotebookFilters
import de.cmdjulian.playground.tag.adapter.TagModel
import de.cmdjulian.playground.tag.adapter.TagUpdateCommand
import de.cmdjulian.playground.tag.domain.Tag
import org.hibernate.persister.collection.OneToManyPersister
import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery
import java.lang.Enum
import kotlin.Suppress

/**
 * Spring native Runtime Aot Config
 */
class AotConfig : RuntimeHintsRegistrar {
    override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
        // Spring
        hints.reflection().registerType(OneToManyPersister::class.java, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS)
        hints.reflection().registerType(FetchableFluentQuery::class.java, *MemberCategory.values())
        hints.reflection().registerType(Enum.EnumDesc::class.java, *MemberCategory.values())

        // Commands
        hints.reflection().registerType(TagModel::class.java, *MemberCategory.values())
        hints.reflection().registerType(TagUpdateCommand::class.java, *MemberCategory.values())
        hints.reflection().registerType(NotebookCommand::class.java, *MemberCategory.values())
        hints.reflection().registerType(NoteCommand::class.java, *MemberCategory.values())

        // Filters
        hints.reflection().registerType(NotebookFilters::class.java, *MemberCategory.values())
        hints.reflection().registerType(NoteFilters::class.java, *MemberCategory.values())

        // Entities
        hints.reflection().registerType(Tag::class.java, *MemberCategory.values())
        hints.reflection().registerType(NoteId::class.java, *MemberCategory.values())
    }
}
