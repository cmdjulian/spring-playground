package de.cmdjulian.playground.config

import de.cmdjulian.playground.note.adapter.NoteFilters
import de.cmdjulian.playground.notebook.adapter.NotebookFilters
import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar

/**
 * Spring native Runtime Aot Config
 */
class AotConfig : RuntimeHintsRegistrar {
    override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
        hints.reflection().registerType(NotebookFilters::class.java, *MemberCategory.values())
        hints.reflection().registerType(NoteFilters::class.java, *MemberCategory.values())
    }
}
