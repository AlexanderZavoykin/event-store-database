package practice.event.store

/**
 * The essential interface. Appendable event log.
 */
interface EventStore {

    /**
     * Appends an event to store.
     *
     * @param event to append
     * @return true, if event was appended successfully, and false, if not.
     */
    fun append(event: Event): Boolean

    /**
     * Fetches records for events happened with an aggregate with given ID before a given moment.
     * If moment is not given, fetches all event records for this aggregate.
     *
     * @param aggregateId aggregate`s ID
     * @param timestamp Unix timestamp in milliseconds
     * @return collection of event records
     */
    fun fetch(aggregateId: String, timestamp: Long? = null): Collection<EventRecord>

}