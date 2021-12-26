package com.hellena.predict.item

import com.hellena.predict.item.category.Category
import com.hellena.predict.item.price.Price
import com.hellena.predict.item.store.Store
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
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

interface ItemRepository: PagingAndSortingRepository<Item, Long> {
    override fun findAll(): List<Item>;

    override fun findAll(pageable: Pageable): Page<Item>
}
