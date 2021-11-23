package com.hellena.predict.item.location

import org.springframework.data.repository.CrudRepository
import javax.persistence.*

@Entity
@Table(name = "location")
data class Location(
    @Id
    @GeneratedValue
    @Column(name = "id")
    val id: Long,

    @Column(name = "street", nullable = false)
    val street: String,

    @Column(name = "house_number", nullable = false)
    val houseNumber: String,

    @Column(name = "city", nullable = false)
    val city: String,

    @Column(name = "postal_code", nullable = false)
    val postalCode: String,

    @Column(name = "district")
    val district: String,

    @Column(name = "country_code", nullable = false)
    val countryCode: String
) {
}

interface LocationRepository: CrudRepository<Location, Long> {
    override fun findAll(): List<Location>
}
