package practice.event.store.impl.postgresql.repository

import org.jooq.DSLContext
import org.jooq.JSONB
import org.jooq.impl.DSL
import practice.event.store.impl.jooq.keys.EVENT_RECORD_AGGREGATE_ID_VERSION_KEY
import practice.event.store.impl.jooq.tables.records.EventRecordRecord
import practice.event.store.impl.jooq.tables.references.EVENT_RECORD

class EventRecordRepository(
    private val dslContext: DSLContext,
) {

    fun fetchLastEventVersion(aggregateId: String): Long? =
        dslContext
            .select(DSL.max(EVENT_RECORD.VERSION))
            .from(EVENT_RECORD)
            .where(EVENT_RECORD.AGGREGATE_ID.eq(aggregateId))
            .fetchOne()
            ?.into(Long::class.java)

    fun insert(eventName: String, aggregateId: String, version: Long, payloadJson: String): Int =
        dslContext
            .insertInto(
                EVENT_RECORD,
                EVENT_RECORD.EVENT_NAME,
                EVENT_RECORD.AGGREGATE_ID,
                EVENT_RECORD.VERSION,
                EVENT_RECORD.TIMESTAMP,
                EVENT_RECORD.PAYLOAD,
            )
            .values(
                eventName,
                aggregateId,
                version,
                System.currentTimeMillis(),
                JSONB.valueOf(payloadJson),
            )
            .onConflictOnConstraint(EVENT_RECORD_AGGREGATE_ID_VERSION_KEY)
            .doNothing()
            .execute()

    fun fetch(aggregateId: String, timestamp: Long?): Collection<EventRecordRecord> =
        dslContext
            .selectFrom(EVENT_RECORD)
            .where(
                EVENT_RECORD.AGGREGATE_ID.eq(aggregateId)
                    .apply {
                        timestamp?.let {
                            this.and(EVENT_RECORD.TIMESTAMP.lessOrEqual(timestamp))
                        }
                    }
            )
            .orderBy(EVENT_RECORD.VERSION.asc())
            .fetch()

}