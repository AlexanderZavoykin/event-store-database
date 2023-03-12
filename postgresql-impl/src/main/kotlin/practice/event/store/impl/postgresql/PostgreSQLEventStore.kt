package practice.event.store.impl.postgresql

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.flywaydb.core.api.configuration.Configuration
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.ThreadLocalTransactionProvider
import practice.event.store.api.Event
import practice.event.store.api.EventRecord
import practice.event.store.api.EventStore
import practice.event.store.impl.postgresql.config.DbConfig
import practice.event.store.impl.postgresql.repository.EventRecordRepository
import java.util.*

class PostgreSQLEventStore(
    dbConfig: DbConfig,
) : EventStore {

    private val repository: EventRecordRepository
    private val objectMapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    init {
        val dslContext = dslContext(dbConfig)
        repository = EventRecordRepository(dslContext)

        val flywayConfig = dbConfig.toFlywayConfig()
        val flyway = Flyway(flywayConfig)
        flyway.migrate()
    }

    override fun append(event: Event): Boolean {
        val aggregateId = event.getAggregateId()

        var result = 0
        var tryNum = 0

        while (result == 0 && tryNum < INSERT_MAX_TRIES) {
            val currentVersion = repository.fetchLastEventVersion(aggregateId)
            val version = currentVersion?.let { it + 1 } ?: 0
            val payloadJson = objectMapper.writeValueAsString(event)

            result = repository.insert(event.getName(), aggregateId, version, payloadJson)
            ++tryNum
        }

        return result > 0
    }

    override fun fetch(aggregateId: String, timestamp: Long?): Collection<EventRecord> =
        repository.fetch(aggregateId, timestamp)
            .map {
                EventRecord(
                    id = it.id!!,
                    eventName = it.eventName!!,
                    aggregateId = aggregateId,
                    version = it.version!!,
                    timestamp = it.timestamp!!,
                    globalId = it.globalId!!,
                    payload = it.payload!!.data(),
                )
            }

    private fun dslContext(dbConfig: DbConfig): DSLContext {
        val hikariConfig = dbConfig.toHikariConfig()
        val hikariDataSource = HikariDataSource(hikariConfig)

        val jooqConfig = DefaultConfiguration().apply {
            setDataSource(hikariDataSource)
            setSQLDialect(SQLDialect.POSTGRES)
            setTransactionProvider(ThreadLocalTransactionProvider(this.connectionProvider()))
        }

        return DSL.using(jooqConfig)
    }

    private fun DbConfig.toHikariConfig(): HikariConfig {
        val props = Properties().apply {
            put("driverClassName", driverClassName)
            put("dataSource.user", dataSourceUser)
            put("dataSource.password", dataSourcePassword)
            put("jdbcUrl", jdbcUrl)
            put("maxLifetime", maxLifetime)
            put("connectionTimeout", connectionTimeout)
            put("idleTimeout", idleTimeout)
            put("maximumPoolSize", maximumPoolSize)
            put("minimumIdle", minimumIdle)
            put("poolName", poolName)
            put("autoCommit", autoCommit)
            put("leakDetectionThreshold", leakDetectionThreshold)
        }
        return HikariConfig(props)
    }

    private fun DbConfig.toFlywayConfig(): Configuration {
        return ClassicConfiguration().apply {
            setDataSource(jdbcUrl, dataSourceUser, dataSourcePassword)
        }
    }

    companion object {
        const val INSERT_MAX_TRIES = 5
    }

}