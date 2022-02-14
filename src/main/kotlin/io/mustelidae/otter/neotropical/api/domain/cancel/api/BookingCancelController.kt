package io.mustelidae.otter.neotropical.api.domain.cancel.api

import io.mustelidae.otter.neotropical.api.common.Reply
import io.mustelidae.otter.neotropical.api.common.toReply
import io.mustelidae.otter.neotropical.api.domain.cancel.BookingCancelInteraction
import io.mustelidae.otter.neotropical.api.domain.cancel.OrderCancelInteraction
import io.mustelidae.otter.neotropical.api.lock.EnableUserLock
import io.mustelidae.otter.neotropical.api.permission.RoleHeader
import io.swagger.v3.oas.annotations.tags.Tag
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Cancel")
@RestController
@RequestMapping("/v1/cancel")
class BookingCancelController(
    private val orderCancelInteraction: OrderCancelInteraction,
    private val bookingCancelInteraction: BookingCancelInteraction
) {

    @EnableUserLock
    @DeleteMapping("/order/{orderId}")
    fun cancel(
        @PathVariable orderId: String,
        @RequestHeader(RoleHeader.XUser.KEY) userId: Long,
        @RequestParam cause: String? = null
    ): Reply<Unit> {
        orderCancelInteraction.cancel(ObjectId(orderId), cause ?: "Cancel at the request of the user.")
        return Unit.toReply()
    }

    @EnableUserLock
    @DeleteMapping("/bookings/{bookingIds}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun cancel(
        @PathVariable bookingIds: List<Long>,
        @RequestHeader(RoleHeader.XUser.KEY) userId: Long,
        @RequestParam cause: String? = null
    ): Reply<Unit> {
        bookingCancelInteraction.cancel(bookingIds, cause ?: "Cancel at the request of the user.")
        return Unit.toReply()
    }
}
