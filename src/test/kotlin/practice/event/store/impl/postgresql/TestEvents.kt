package practice.event.store.impl.postgresql

import practice.event.store.Event
import java.time.Instant
import java.util.*

data class OrderAcceptedEvent(
    val orderId: String = randomUUID(),
    val customerId: String = randomUUID(),
) : Event {
    override fun getName(): String = "ORDER_ACCEPTED_EVENT"
    override fun getAggregateId(): String = orderId
}

data class PaymentConfirmed(
    val paymentId: String = randomUUID(),
    val customerId: String = randomUUID(),
    val confirmationDate: Instant = Instant.now(),
) : Event {
    override fun getName(): String = "PAYMENT_CONFIRMED_EVENT"
    override fun getAggregateId(): String = paymentId
}

private fun randomUUID(): String = UUID.randomUUID().toString()