package practice.event.store.impl.postgresql

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.assertj.core.api.Assertions.assertThat
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.ThreadLocalTransactionProvider
import org.junit.jupiter.api.Test
import practice.event.store.impl.jooq.tables.records.EventRecordRecord
import practice.event.store.impl.jooq.tables.references.EVENT_RECORD
import practice.event.store.impl.postgresql.config.DbConfig
import java.util.*

class PostgreSQLEventStoreTest {

    private val eventStore: PostgreSQLEventStore
    private val dslContext: DSLContext
    private val objectMapper = jacksonObjectMapper()

    init {
        val dbConfig = DbConfig(
            driverClassName = "org.postgresql.Driver",
            dataSourceUser = "event-store",
            dataSourcePassword = "event-store",
            jdbcUrl = "jdbc:postgresql://127.0.0.1:5432/event-store",
            poolName = "es",
        )
        dslContext = dslContext(dbConfig)
        eventStore = PostgreSQLEventStore(dbConfig)
    }

    @Test
    fun `append event`() {
        val orderAcceptedEvent = OrderAcceptedEvent()

        eventStore.append(orderAcceptedEvent)

        val records = findRecordsByAggregateId(orderAcceptedEvent.getAggregateId())

        assertThat(records).hasSize(1)

        val record = records[0]
        assertThat(record.id).isNotNull
        assertThat(record.eventName).isEqualTo(orderAcceptedEvent.getName())
        assertThat(record.version).isEqualTo(0)
        assertThat(record.timestamp).isNotNull
        assertThat(record.globalId).isNotNull
        assertThat(record.payload).isNotNull

        val payloadValue = objectMapper.readValue(record.payload!!.data(), OrderAcceptedEvent::class.java)
        assertThat(payloadValue).isEqualTo(orderAcceptedEvent)
    }

    private fun findRecordsByAggregateId(aggregateId: String): List<EventRecordRecord> =
        dslContext.selectFrom(EVENT_RECORD)
            .where(EVENT_RECORD.AGGREGATE_ID.eq(aggregateId))
            .fetch()

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

}