/*
 * This file is generated by jOOQ.
 */
package practice.event.store.impl.jooq.tables.records


import org.jooq.Field
import org.jooq.JSONB
import org.jooq.Record1
import org.jooq.Record7
import org.jooq.Row7
import org.jooq.impl.UpdatableRecordImpl

import practice.event.store.impl.jooq.tables.EventRecord


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class EventRecordRecord() : UpdatableRecordImpl<EventRecordRecord>(EventRecord.EVENT_RECORD), Record7<Long?, String?, String?, Long?, Long?, Long?, JSONB?> {

    open var id: Long?
        set(value): Unit = set(0, value)
        get(): Long? = get(0) as Long?

    open var eventName: String?
        set(value): Unit = set(1, value)
        get(): String? = get(1) as String?

    open var aggregateId: String?
        set(value): Unit = set(2, value)
        get(): String? = get(2) as String?

    open var version: Long?
        set(value): Unit = set(3, value)
        get(): Long? = get(3) as Long?

    open var timestamp: Long?
        set(value): Unit = set(4, value)
        get(): Long? = get(4) as Long?

    open var globalId: Long?
        set(value): Unit = set(5, value)
        get(): Long? = get(5) as Long?

    open var payload: JSONB?
        set(value): Unit = set(6, value)
        get(): JSONB? = get(6) as JSONB?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Long?> = super.key() as Record1<Long?>

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row7<Long?, String?, String?, Long?, Long?, Long?, JSONB?> = super.fieldsRow() as Row7<Long?, String?, String?, Long?, Long?, Long?, JSONB?>
    override fun valuesRow(): Row7<Long?, String?, String?, Long?, Long?, Long?, JSONB?> = super.valuesRow() as Row7<Long?, String?, String?, Long?, Long?, Long?, JSONB?>
    override fun field1(): Field<Long?> = EventRecord.EVENT_RECORD.ID
    override fun field2(): Field<String?> = EventRecord.EVENT_RECORD.EVENT_NAME
    override fun field3(): Field<String?> = EventRecord.EVENT_RECORD.AGGREGATE_ID
    override fun field4(): Field<Long?> = EventRecord.EVENT_RECORD.VERSION
    override fun field5(): Field<Long?> = EventRecord.EVENT_RECORD.TIMESTAMP
    override fun field6(): Field<Long?> = EventRecord.EVENT_RECORD.GLOBAL_ID
    override fun field7(): Field<JSONB?> = EventRecord.EVENT_RECORD.PAYLOAD
    override fun component1(): Long? = id
    override fun component2(): String? = eventName
    override fun component3(): String? = aggregateId
    override fun component4(): Long? = version
    override fun component5(): Long? = timestamp
    override fun component6(): Long? = globalId
    override fun component7(): JSONB? = payload
    override fun value1(): Long? = id
    override fun value2(): String? = eventName
    override fun value3(): String? = aggregateId
    override fun value4(): Long? = version
    override fun value5(): Long? = timestamp
    override fun value6(): Long? = globalId
    override fun value7(): JSONB? = payload

    override fun value1(value: Long?): EventRecordRecord {
        this.id = value
        return this
    }

    override fun value2(value: String?): EventRecordRecord {
        this.eventName = value
        return this
    }

    override fun value3(value: String?): EventRecordRecord {
        this.aggregateId = value
        return this
    }

    override fun value4(value: Long?): EventRecordRecord {
        this.version = value
        return this
    }

    override fun value5(value: Long?): EventRecordRecord {
        this.timestamp = value
        return this
    }

    override fun value6(value: Long?): EventRecordRecord {
        this.globalId = value
        return this
    }

    override fun value7(value: JSONB?): EventRecordRecord {
        this.payload = value
        return this
    }

    override fun values(value1: Long?, value2: String?, value3: String?, value4: Long?, value5: Long?, value6: Long?, value7: JSONB?): EventRecordRecord {
        this.value1(value1)
        this.value2(value2)
        this.value3(value3)
        this.value4(value4)
        this.value5(value5)
        this.value6(value6)
        this.value7(value7)
        return this
    }

    /**
     * Create a detached, initialised EventRecordRecord
     */
    constructor(id: Long? = null, eventName: String? = null, aggregateId: String? = null, version: Long? = null, timestamp: Long? = null, globalId: Long? = null, payload: JSONB? = null): this() {
        this.id = id
        this.eventName = eventName
        this.aggregateId = aggregateId
        this.version = version
        this.timestamp = timestamp
        this.globalId = globalId
        this.payload = payload
    }
}
