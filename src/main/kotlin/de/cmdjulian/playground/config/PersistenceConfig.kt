package de.cmdjulian.playground.config

import de.cmdjulian.playground.config.PersistenceConfig.Companion.TEMPORAL_PROVIDER
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.time.OffsetDateTime
import java.util.*

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = TEMPORAL_PROVIDER)
class PersistenceConfig {

    companion object {
        const val TEMPORAL_PROVIDER = "TemporalProvider"
    }

    @Bean(TEMPORAL_PROVIDER)
    fun temporalProvider() = DateTimeProvider { Optional.of(OffsetDateTime.now()) }
}
