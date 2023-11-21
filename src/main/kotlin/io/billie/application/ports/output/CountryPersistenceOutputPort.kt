package io.billie.application.ports.output

import io.billie.infrastructure.adapters.input.contries.data.CityResponse
import io.billie.infrastructure.adapters.input.contries.data.CountryResponse

interface CountryPersistenceOutputPort {

    fun findCountries(): List<CountryResponse>

    fun findCities(countryCode: String): List<CityResponse>

}