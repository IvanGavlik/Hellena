package com.hellena.predict.item

import com.hellena.predict.item.category.Category
import com.hellena.predict.item.price.Price
import com.hellena.predict.item.store.Store
import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
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

interface SearchItemRepository {
    fun search(search: ItemSearch): List<Item>;
}

class SearchItemRepositoryImpl(
    @PersistenceContext
    val entityManager: EntityManager
): SearchItemRepository {

    override fun search(search: ItemSearch): MutableList<Item> {
        val query = JPAQueryFactory(this.entityManager); //TODO move to instance filed
        val item = QItem.item;

        // TODO sort and pagination
        return query.selectFrom(item)
            .where( buildPredicate(search, item) )
            .fetch();
    }

    private fun buildPredicate(search: ItemSearch, item: QItem): BooleanBuilder {

        val queryBuilder = BooleanBuilder();

        if (search.name != null) {
            queryBuilder.and( item.name.contains(search.name) )
        }
        if (search.categoryId != null) {
            queryBuilder.and( item.category.id.eq(search.categoryId) )
        }
        if (search.cityId != null) {
            queryBuilder.and( item.store.location.id.eq(search.cityId))
        }
        if (search.priceMIn != null && search.priceMIn.toDouble() > 0) {
            queryBuilder.and( item.price.actionPrice.goe(search.priceMIn))
        }
        if (search.priceMax != null && search.priceMax.toDouble() > 0) {
            queryBuilder.and( item.price.actionPrice.loe(search.priceMax))
        }

        return queryBuilder;
    }

}

interface ItemRepository: PagingAndSortingRepository<Item, Long>, SearchItemRepository {
    override fun findAll(): List<Item>;

    override fun findAll(pageable: Pageable): Page<Item>
}

