package practice.event.store.impl.postgresql

import practice.event.store.Event
import java.util.*

data class OrderAcceptedEvent(
    val orderId: String = randomUUID(),
    val customerId: String = randomUUID(),
) : Event {
    override fun getName(): String = "ORDER_ACCEPTED_EVENT"
    override fun getAggregateId(): String = customerId
}

data class PaymentConfirmedEvent(
    val paymentId: String = randomUUID(),
    val customerId: String = randomUUID(),
    val confirmationDate: Long = System.currentTimeMillis(),
) : Event {
    override fun getName(): String = "PAYMENT_CONFIRMED_EVENT"
    override fun getAggregateId(): String = customerId
}

private fun randomUUID(): String = UUID.randomUUID().toString()