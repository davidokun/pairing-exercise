package io.billie.infrastructure.adapters.input.orders

import io.billie.infrastructure.adapters.input.exception.ApiError
import io.billie.domain.exception.InvalidOrganisationId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class OrderExceptionAdvice : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [InvalidOrganisationId::class])
    protected fun handleInvalidOrganisationId(
        ex: RuntimeException?, request: WebRequest?
    ): ResponseEntity<Any> {
        val apiError = ApiError(ex?.message, HttpStatus.BAD_REQUEST.value())
        return ResponseEntity<Any>(apiError, HttpStatus.BAD_REQUEST)
    }
}