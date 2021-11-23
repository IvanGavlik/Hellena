package com.hellena.predict.item.store

import com.hellena.predict.item.location.Location
import org.springframework.data.repository.CrudRepository
import javax.persistence.*


@Entity
@Table(name = "store")
data class Store(
    @Id
    @GeneratedValue
    @Column(name = "id")
    val id: Long,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "company", nullable = false)
    val companyName: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="location_id")
    val location: Location
    ) {
}

interface StoreRepository: CrudRepository<Store, Long> {
    override fun findAll(): List<Store>;
}
