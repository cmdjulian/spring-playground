package de.cmdjulian.playground

import de.cmdjulian.playground.config.AotConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ImportRuntimeHints

@SpringBootApplication
@ImportRuntimeHints(AotConfig::class)
class NotesApplication

fun main(args: Array<String>) {
    runApplication<NotesApplication>(*args)
}
