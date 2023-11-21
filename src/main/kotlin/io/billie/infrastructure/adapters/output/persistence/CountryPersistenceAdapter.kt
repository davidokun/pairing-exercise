package io.billie.infrastructure.adapters.output.persistence

import io.billie.application.ports.output.CountryPersistenceOutputPort
import io.billie.infrastructure.adapters.input.contries.data.CityResponse
import io.billie.infrastructure.adapters.input.contries.data.CountryResponse
import io.billie.infrastructure.adapters.output.persistence.repository.CityRepository
import io.billie.infrastructure.adapters.output.persistence.repository.CountryRepository
import org.springframework.stereotype.Component

@Component
class CountryPersistenceAdapter(
    val dbCountry: CountryRepository,
    val dbCity: CityRepository,
) : CountryPersistenceOutputPort {

    override fun findCountries(): List<CountryResponse> {
        return dbCountry.findCountries()
    }

    override fun findCities(countryCode: String): List<CityResponse> {
        return dbCity.findByCountryCode(countryCode)
    }
}