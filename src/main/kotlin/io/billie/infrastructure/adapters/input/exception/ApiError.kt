package io.billie.infrastructure.adapters.input.exception

class ApiError(
    var message: String? = null,
    var status: Int? = null
)