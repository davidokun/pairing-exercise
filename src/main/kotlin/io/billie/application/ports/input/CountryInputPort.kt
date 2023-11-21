package io.billie.application.ports.input

import io.billie.infrastructure.adapters.input.contries.data.CityResponse
import io.billie.infrastructure.adapters.input.contries.data.CountryResponse

interface CountryInputPort {

    fun findCountries(): List<CountryResponse>

    fun findCities(countryCode: String): List<CityResponse>
}