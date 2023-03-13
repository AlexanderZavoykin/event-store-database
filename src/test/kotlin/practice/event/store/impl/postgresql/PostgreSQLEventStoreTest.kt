package practice.event.store.impl.postgresql

import com.fasterxml.jackson.databind.DeserializationFeature
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
import practice.event.store.PostgreSQLEventStore
import practice.event.store.config.DbConfig
import practice.event.store.persistence.tables.records.EventRecordRecord
import practice.event.store.persistence.tables.references.EVENT_RECORD
import java.io.File
import java.util.*
import javax.sql.DataSource

class PostgreSQLEventStoreTest {

    private val eventStore: PostgreSQLEventStore
    private val dslContext: DSLContext
    private val objectMapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    init {
        val dbConfig = objectMapper.readValue(File("src/test/resources/config.json"), DbConfig::class.java)
        val dataSource = dataSource(dbConfig)
        dslContext = dslContext(dataSource)
        eventStore = PostgreSQLEventStore(dataSource)
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

    @Test
    fun `fetch event records`() {
        val orderAcceptedEvent = OrderAcceptedEvent()
        val aggregateId = orderAcceptedEvent.getAggregateId()

        val paymentConfirmedEvent = PaymentConfirmedEvent(customerId = aggregateId)

        eventStore.append(orderAcceptedEvent)
        eventStore.append(paymentConfirmedEvent)

        val records = findRecordsByAggregateId(orderAcceptedEvent.getAggregateId())

        assertThat(records).hasSize(2)
        assertThat(records).anyMatch { it.version!! == 0L }
        assertThat(records).anyMatch { it.version!! == 1L }
    }

    private fun findRecordsByAggregateId(aggregateId: String): List<EventRecordRecord> =
        dslContext.selectFrom(EVENT_RECORD)
            .where(EVENT_RECORD.AGGREGATE_ID.eq(aggregateId))
            .fetch()

    private fun dataSource(dbConfig: DbConfig): DataSource =
        HikariDataSource(dbConfig.toHikariConfig())

    private fun dslContext(dataSource: DataSource): DSLContext {
        val jooqConfig = DefaultConfiguration().apply {
            setDataSource(dataSource)
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