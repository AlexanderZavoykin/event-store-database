package practice.event.store

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.flywaydb.core.api.configuration.Configuration
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.ThreadLocalTransactionProvider
import practice.event.store.repository.EventRecordRepository
import javax.sql.DataSource

class PostgreSQLEventStore(
    dataSource: DataSource,
) : EventStore {

    private val repository: EventRecordRepository
    private val objectMapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    init {
        val dslContext = dslContext(dataSource)
        repository = EventRecordRepository(dslContext)

        val flywayConfig = flywayConfig(dataSource)
        val flyway = Flyway(flywayConfig)
        flyway.migrate()
    }

    override fun append(event: Event): Boolean {
        val aggregateId = event.getAggregateId()

        var result = 0
        var tryNum = 0

        while (result == 0 && tryNum < INSERT_MAX_TRIES) {
            val currentVersion = repository.fetchLastEventVersionOrNull(aggregateId)
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

    private fun dslContext(dataSource: DataSource): DSLContext {
        val jooqConfig = DefaultConfiguration()
            .apply {
                setDataSource(dataSource)
                setSQLDialect(SQLDialect.POSTGRES)
                setTransactionProvider(ThreadLocalTransactionProvider(this.connectionProvider()))
            }

        return DSL.using(jooqConfig)
    }

    private fun flywayConfig(dataSource: DataSource): Configuration =
        ClassicConfiguration()
            .apply { setDataSource(dataSource) }

    companion object {
        const val INSERT_MAX_TRIES = 5
    }

}