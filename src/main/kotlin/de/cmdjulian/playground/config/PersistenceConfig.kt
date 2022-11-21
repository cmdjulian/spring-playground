package de.cmdjulian.playground.config

import de.cmdjulian.playground.NotesApplication
import de.cmdjulian.playground.config.PersistenceConfig.Companion.TEMPORAL_PROVIDER
import mu.KotlinLogging
import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.springframework.aop.framework.ProxyFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean
import org.springframework.data.repository.Repository
import java.time.OffsetDateTime
import java.util.*

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = TEMPORAL_PROVIDER)
@EnableJpaRepositories(
    basePackageClasses = [NotesApplication::class],
    repositoryFactoryBeanClass = JpaRepositoryFactoryBeanImpl::class
)
class PersistenceConfig {

    internal companion object {
        const val TEMPORAL_PROVIDER = "TemporalProvider"
    }

    @Bean(TEMPORAL_PROVIDER)
    fun temporalProvider() = DateTimeProvider { Optional.of(OffsetDateTime.now()) }
}

private object LoggingMethodInterceptor : MethodInterceptor {

    private val logger = KotlinLogging.logger { }

    override fun invoke(invocation: MethodInvocation): Any? {
        return invocation.proceed().also {
            logger.info { "called method ${invocation.method.name} with args '${invocation.arguments}'" }
        }
    }
}

private class JpaRepositoryFactoryBeanImpl<R : Repository<E, I>, E, I>(repositoryInterface: Class<out R>) :
    JpaRepositoryFactoryBean<R, E, I>(repositoryInterface) {

    @Suppress("UNCHECKED_CAST")
    override fun getObject(): R {
        val repository = super.getObject()

        val factory = ProxyFactory(repository).apply {
            addAdvice(LoggingMethodInterceptor)
            isFrozen = true
            isPreFiltered = true
            isOpaque = true
            isExposeProxy = false
            isOptimize = true
        }

        return factory.proxy as R
    }
}
