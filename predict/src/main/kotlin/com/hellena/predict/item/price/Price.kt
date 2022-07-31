package com.hellena.predict.item.price

import org.springframework.data.repository.CrudRepository
import java.math.BigDecimal
import java.time.LocalDate
import javax.persistence.*

@Embeddable
data class Price(
    @Column(name = "price")
    val originalPrice: BigDecimal?,

    @Column(name = "action_price", nullable = false)
    val actionPrice: BigDecimal,

    @Column(name = "active_from", nullable = false)
    val activeFrom: LocalDate,

    @Column(name = "active_to", nullable = false)
    val activeTo: LocalDate,
)
