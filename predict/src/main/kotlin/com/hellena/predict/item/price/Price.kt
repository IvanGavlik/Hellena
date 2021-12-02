package com.hellena.predict.item.price

import org.springframework.data.repository.CrudRepository
import java.math.BigDecimal
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "price")
data class Price(
    @Id
    @GeneratedValue
    @Column(name = "id")
    val id: Long,

    @Column(name = "price", nullable = false)
    val originalPrice: BigDecimal,

    @Column(name = "action_price", nullable = false)
    val actionPrice: BigDecimal,

    @Column(name = "active_from", nullable = false)
    val activeFrom: LocalDate,

    @Column(name = "active_to", nullable = false)
    val activeTo: LocalDate,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous")
    val previous: Price,
) {
}

interface PriceRepository: CrudRepository<Price, Long> {
    override fun findAll(): List<Price>;
}
