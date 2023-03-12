package practice.event.store.api

/**
 * An event happened with an aggregate.
 */
interface Event {

    /**
     * Gives name of events` family this event belongs to. E.g. 'ORDER_CREATED', 'PAYMENT_CANCELLED', etc.
     *
     * @return
     */
    fun getName(): String

    /**
     * Gives ID of an aggregate this event happened with.
     *
     * @return aggregate`s ID
     */
    fun getAggregateId(): String
}