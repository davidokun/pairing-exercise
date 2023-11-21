package io.billie.infrastructure.adapters.input.orders

import io.billie.application.ports.input.OrderInputPort
import io.billie.domain.order.model.order.Order
import io.billie.infrastructure.adapters.input.common.Entity
import io.billie.infrastructure.adapters.input.orders.data.OrderRequest
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("orders")
class OrderResource(val orderInputPort: OrderInputPort) {

    @PostMapping
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Accepted the new order from organisation",
                content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = Entity::class)))
                    ))]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun post(@Valid @RequestBody orderRequest: OrderRequest): Entity {
        val id = orderInputPort.createOrder(
            Order(
                orderDate = orderRequest.orderDate,
                totalAmount = orderRequest.orderAmount,
                organisationId = UUID.fromString(orderRequest.organisationId)
            )
        )
        return Entity(id)
    }

}