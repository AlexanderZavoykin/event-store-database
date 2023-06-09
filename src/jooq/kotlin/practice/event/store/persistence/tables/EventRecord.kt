/*
 * This file is generated by jOOQ.
 */
package practice.event.store.persistence.tables


import java.util.function.Function

import kotlin.collections.List

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Identity
import org.jooq.JSONB
import org.jooq.Name
import org.jooq.Record
import org.jooq.Records
import org.jooq.Row7
import org.jooq.Schema
import org.jooq.SelectField
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl

import practice.event.store.persistence.Public
import practice.event.store.persistence.keys.EVENT_RECORD_AGGREGATE_ID_VERSION_KEY
import practice.event.store.persistence.keys.EVENT_RECORD_PKEY
import practice.event.store.persistence.tables.records.EventRecordRecord


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class EventRecord(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, EventRecordRecord>?,
    aliased: Table<EventRecordRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<EventRecordRecord>(
    alias,
    Public.PUBLIC,
    child,
    path,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table()
) {
    companion object {

        /**
         * The reference instance of <code>public.event_record</code>
         */
        val EVENT_RECORD: EventRecord = EventRecord()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<EventRecordRecord> = EventRecordRecord::class.java

    /**
     * The column <code>public.event_record.id</code>.
     */
    val ID: TableField<EventRecordRecord, Long?> = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "")

    /**
     * The column <code>public.event_record.event_name</code>.
     */
    val EVENT_NAME: TableField<EventRecordRecord, String?> = createField(DSL.name("event_name"), SQLDataType.CLOB.nullable(false), this, "")

    /**
     * The column <code>public.event_record.aggregate_id</code>.
     */
    val AGGREGATE_ID: TableField<EventRecordRecord, String?> = createField(DSL.name("aggregate_id"), SQLDataType.CLOB.nullable(false), this, "")

    /**
     * The column <code>public.event_record.version</code>.
     */
    val VERSION: TableField<EventRecordRecord, Long?> = createField(DSL.name("version"), SQLDataType.BIGINT.nullable(false), this, "")

    /**
     * The column <code>public.event_record.timestamp</code>.
     */
    val TIMESTAMP: TableField<EventRecordRecord, Long?> = createField(DSL.name("timestamp"), SQLDataType.BIGINT.nullable(false), this, "")

    /**
     * The column <code>public.event_record.global_id</code>.
     */
    val GLOBAL_ID: TableField<EventRecordRecord, Long?> = createField(DSL.name("global_id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "")

    /**
     * The column <code>public.event_record.payload</code>.
     */
    val PAYLOAD: TableField<EventRecordRecord, JSONB?> = createField(DSL.name("payload"), SQLDataType.JSONB.nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<EventRecordRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<EventRecordRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>public.event_record</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>public.event_record</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>public.event_record</code> table reference
     */
    constructor(): this(DSL.name("event_record"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, EventRecordRecord>): this(Internal.createPathAlias(child, key), child, key, EVENT_RECORD, null)
    override fun getSchema(): Schema? = if (aliased()) null else Public.PUBLIC
    override fun getIdentity(): Identity<EventRecordRecord, Long?> = super.getIdentity() as Identity<EventRecordRecord, Long?>
    override fun getPrimaryKey(): UniqueKey<EventRecordRecord> = EVENT_RECORD_PKEY
    override fun getUniqueKeys(): List<UniqueKey<EventRecordRecord>> = listOf(EVENT_RECORD_AGGREGATE_ID_VERSION_KEY)
    override fun `as`(alias: String): EventRecord = EventRecord(DSL.name(alias), this)
    override fun `as`(alias: Name): EventRecord = EventRecord(alias, this)
    override fun `as`(alias: Table<*>): EventRecord = EventRecord(alias.getQualifiedName(), this)

    /**
     * Rename this table
     */
    override fun rename(name: String): EventRecord = EventRecord(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): EventRecord = EventRecord(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): EventRecord = EventRecord(name.getQualifiedName(), null)

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row7<Long?, String?, String?, Long?, Long?, Long?, JSONB?> = super.fieldsRow() as Row7<Long?, String?, String?, Long?, Long?, Long?, JSONB?>

    /**
     * Convenience mapping calling {@link #convertFrom(Function)}.
     */
    fun <U> mapping(from: (Long?, String?, String?, Long?, Long?, Long?, JSONB?) -> U): SelectField<U> = convertFrom(Records.mapping(from))

    /**
     * Convenience mapping calling {@link #convertFrom(Class, Function)}.
     */
    fun <U> mapping(toType: Class<U>, from: (Long?, String?, String?, Long?, Long?, Long?, JSONB?) -> U): SelectField<U> = convertFrom(toType, Records.mapping(from))
}
