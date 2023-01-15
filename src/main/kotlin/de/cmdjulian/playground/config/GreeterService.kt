package de.cmdjulian.playground.config

import jakarta.annotation.PostConstruct
import mu.KotlinLogging
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource

@ConfigurationProperties("hello")
data class Config(val message: Resource)

@Configuration
@EnableConfigurationProperties(Config::class)
@ImportRuntimeHints(ResourceConfigRegistrar::class)
class GreeterService(private val config: Config) {

    private val logger = KotlinLogging.logger { }

    @PostConstruct
    fun greet() {
        logger.info { config.message.file.readText() }
    }
}

internal class ResourceConfigRegistrar : RuntimeHintsRegistrar {
    override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
        hints.resources().registerResource(ClassPathResource("hello/world.txt"))
    }
}
