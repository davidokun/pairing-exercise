package io.billie.infrastructure.adapters.input.shipment

import io.billie.application.ports.input.ShipmentNotificationInputPort
import io.billie.infrastructure.adapters.input.common.Entity
import io.billie.infrastructure.adapters.input.shipment.data.ShipmentNotificationRequest
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("shipments")
class ShipmentNotificationResource(val shipmentService: ShipmentNotificationInputPort) {

    @PostMapping
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Accepted the new shipment from organisation",
                content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = Entity::class)))
                    ))]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun postOrderShipment(
        @Valid @RequestBody shipmentNotificationRequest: ShipmentNotificationRequest
    ): Entity {
        val id = shipmentService.notify(shipmentNotificationRequest)
        return Entity(id)
    }
}