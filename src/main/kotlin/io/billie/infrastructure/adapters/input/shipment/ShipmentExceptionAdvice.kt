package io.billie.infrastructure.adapters.input.shipment

import io.billie.infrastructure.adapters.input.exception.ApiError
import io.billie.domain.exception.InvalidShipmentAmount
import io.billie.domain.exception.OrderNotFound
import io.billie.domain.exception.ShipmentOrderAlreadyCompleted
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ShipmentExceptionAdvice : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [OrderNotFound::class])
    protected fun handleOrderNotFound(
        ex: RuntimeException?, request: WebRequest?
    ): ResponseEntity<Any> {
        val apiError = ApiError("Order Not Found", HttpStatus.NOT_FOUND.value())
        return ResponseEntity<Any>(apiError, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [InvalidShipmentAmount::class])
    protected fun handleInvalidShipmentAmount(
        ex: RuntimeException?, request: WebRequest?
    ): ResponseEntity<Any> {
        val apiError = ApiError(ex?.message, HttpStatus.BAD_REQUEST.value())
        return ResponseEntity<Any>(apiError, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [ShipmentOrderAlreadyCompleted::class])
    protected fun handleShipmentOrderAlreadyCompleted(
        ex: RuntimeException?, request: WebRequest?
    ): ResponseEntity<Any> {
        val apiError = ApiError(ex?.message, HttpStatus.CONFLICT.value())
        return ResponseEntity<Any>(apiError, HttpStatus.CONFLICT)
    }
}