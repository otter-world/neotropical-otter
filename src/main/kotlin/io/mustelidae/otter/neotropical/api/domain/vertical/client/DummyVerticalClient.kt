package io.mustelidae.otter.neotropical.api.domain.vertical.client

import io.mustelidae.otter.neotropical.api.common.design.v1.component.DisplayCard
import io.mustelidae.otter.neotropical.api.config.AppEnvironment
import io.mustelidae.otter.neotropical.api.config.DevelopMistakeException
import io.mustelidae.otter.neotropical.api.domain.booking.Booking
import io.mustelidae.otter.neotropical.api.domain.booking.api.gateway.Label
import io.mustelidae.otter.neotropical.api.domain.order.OrderSheet
import io.mustelidae.otter.neotropical.api.domain.vertical.CallOffBooking
import io.mustelidae.otter.neotropical.api.domain.vertical.CancellationUnit
import io.mustelidae.otter.neotropical.api.domain.vertical.ExchangeResult
import io.mustelidae.otter.neotropical.api.domain.vertical.client.design.v1.RecordCard
import io.mustelidae.otter.neotropical.api.domain.vertical.client.design.v1.VerticalRecord
import kotlin.random.Random

class DummyVerticalClient(
    private val productEnv: AppEnvironment.Product
) : VerticalClient {
    override fun cancel(userId: Long, bookingIds: List<Long>, cause: String): ExchangeResult {
        return if (productEnv.dummyControl.cancel)
            ExchangeResult(true)
        else
            ExchangeResult(false, "Cancellation is not possible due to testing.")
    }

    override fun cancelByItem(cancellationUnit: CancellationUnit, cause: String): ExchangeResult {
        return if (productEnv.dummyControl.cancelByItem)
            ExchangeResult(true)
        else
            ExchangeResult(false, "Cancellation is not possible due to testing.")
    }

    override fun findRecord(bookingId: Long): VerticalRecord {
        return VerticalRecord(
            Random.nextLong(),
            bookingId,
            Label("Completed!!", true, Label.Color.RED),
            RecordCard(
                listOf(
                    DisplayCard(
                        1,
                        "service",
                        "dummy great service",
                        listOf(
                            DisplayCard.Content(
                                "Address",
                                DisplayCard.Content.Type.TEXT,
                                DisplayCard.Content.ContentBox("630 W. 5th Street, Los Angeles, CA")
                            )
                        )
                    ),
                    DisplayCard(
                        2,
                        "22-02-23",
                        "Department",
                        listOf(
                            DisplayCard.Content(
                                "Name",
                                DisplayCard.Content.Type.TEXT,
                                DisplayCard.Content.ContentBox("Otter world company")
                            ),
                            DisplayCard.Content(
                                "Phone",
                                DisplayCard.Content.Type.CALL,
                                DisplayCard.Content.ContentBox(call = DisplayCard.Content.ContentBox.Call("Mr. Otter", "+01341-34524"))
                            )
                        )
                    )
                )
            ),
            null
        )
    }

    override fun obtain(bookings: List<Booking>, orderSheet: OrderSheet): ExchangeResult {
        return if (productEnv.dummyControl.obtainApproval)
            ExchangeResult(true)
        else
            ExchangeResult(false, "Cancellation is not possible due to testing.")
    }

    override fun askWhetherCallOff(userId: Long, bookingId: Long): CallOffBooking {
        return if (productEnv.dummyControl.callOff)
            CallOffBooking(true, 0, 0)
        else
            CallOffBooking(true, 500, 500)
    }

    init {
        if (productEnv.useDummy.not())
            throw DevelopMistakeException("The application environment variable(use-dummy) is set incorrectly.")
    }
}
