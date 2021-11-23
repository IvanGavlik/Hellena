package com.hellena.predict.item

import com.hellena.predict.item.category.Category
import com.hellena.predict.item.price.Price
import com.hellena.predict.item.store.Store
import org.springframework.data.repository.CrudRepository
import javax.persistence.*

@Entity
@Table(name = "item")
data class Item(
    @Id
    @GeneratedValue
    @Column(name = "id")
    val id: Long,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "long_name", nullable = false)
    val longName: String,

    @Column(name = "description", nullable = false)
    val description: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    val category: Category,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    val store: Store,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    val price: Price,
    ) {
}

interface ItemRepository: CrudRepository<Item, Long> {
    override fun findAll(): List<Item>;
}
