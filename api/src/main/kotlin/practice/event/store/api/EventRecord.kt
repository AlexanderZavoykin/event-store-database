package practice.event.store.api

/**
 * A record saved in an event store.
 *
 * @property id event store`s id of event
 * @property eventName name of family event belongs to
 * @property aggregateId ID of aggregate event happened with
 * @property version numerical order among events happened with the same aggregate
 * @property timestamp Unix timestamp in milliseconds representing a moment of saving record to event store
 * @property globalId global numerical order in event store
 * @property payload event`s payload
 */
data class EventRecord(
    val id: Long,
    val eventName: String,
    val aggregateId: String,
    val version: Long,
    val timestamp: Long,
    val globalId: Long,
    val payload: String,
)
