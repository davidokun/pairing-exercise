package io.billie.domain.country.service

import io.billie.application.ports.input.CountryInputPort
import io.billie.application.ports.output.CountryPersistenceOutputPort
import io.billie.infrastructure.adapters.input.contries.data.CityResponse
import io.billie.infrastructure.adapters.input.contries.data.CountryResponse
import org.springframework.stereotype.Service

@Service
class CountryService(
    val countryPersistenceAdapter: CountryPersistenceOutputPort
) : CountryInputPort {

    override fun findCountries(): List<CountryResponse> {
        return countryPersistenceAdapter.findCountries()
    }

    override fun findCities(countryCode: String): List<CityResponse> =
        countryPersistenceAdapter.findCities(countryCode)

}
