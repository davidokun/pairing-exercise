package io.billie.domain.exception

class OrderNotFound(val orderId: String) : RuntimeException()