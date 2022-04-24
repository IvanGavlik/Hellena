package com.hellena.predict.item

import com.hellena.predict.item.category.Category
import com.hellena.predict.item.price.Price
import com.hellena.predict.item.store.Store
import com.hellena.predict.search.Paginator
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "item")
data class Item(

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "long_name", nullable = false)
    val longName: String,

    @Column(name = "description", nullable = false)
    val description: String,

    @Column(name ="image_id", nullable = true)
    val imageId: String?,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    val category: Category,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    val store: Store,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    val price: Price,

    @Column(name ="user_name", nullable = true)
    val userName: String,
    ) {

    @Id
    @GeneratedValue
    @Column(name = "id")
    var id: Long? = null;

    val discountPrice: BigDecimal?
    @javax.persistence.Transient()
    get() {
        if(this.price.originalPrice != null && this.price.actionPrice != null) {
            return this.price.originalPrice.minus(this.price.actionPrice);
        }
        return null;
    }

    val discountPercentage: BigDecimal?
    @javax.persistence.Transient()
    get() {
        val originalPrice: BigDecimal? = this.price.originalPrice;
        val actionPrice: BigDecimal = this.price.actionPrice;
        // 100 - ((actionPrice / originalPrice) * 100)
        if(originalPrice != null && actionPrice != null) {
            val hundred = BigDecimal(100)
            return hundred.minus(
                (actionPrice.divide(originalPrice, RoundingMode.HALF_UP))
                    .multiply(hundred)
            )
        }
        return null
    }
}

interface SearchItemRepository {
    fun search(search: ItemSearch): Paginator<Item>;

    fun searchDiscountExistOrderByBiggestAsc(search: ItemSearch): Paginator<Item>;
}

class SearchItemRepositoryImpl(
    @PersistenceContext
    val entityManager: EntityManager
): SearchItemRepository {

    private val query = JPAQueryFactory(this.entityManager)

    override fun search(search: ItemSearch): Paginator<Item> {
        val item = QItem.item;

        val predicate = buildSimpleSearchPredicate(search, item);

        val result = query.selectFrom(item)
            .where(predicate)
            .orderBy(OrderSpecifier(Order.ASC, item.name))
            .offset(search.page.getIndex())
            .limit(search.page.getSize()) // TODO WHAT IF PAGE SIZE IS NOT SET
            .fetchResults()
        return Paginator<Item>(result.total, result.results)
    }

    override fun searchDiscountExistOrderByBiggestAsc(search: ItemSearch): Paginator<Item> {
        val item = QItem.item;

        val predicate = buildDiscountSearchPredicate(search, item);

        val result = query.selectFrom(item)
            .where(predicate)
            .offset(search.page.getIndex())
            .limit(search.page.getSize()) // TODO WHAT IF PAGE SIZE IS NOT SET
            .fetchResults()
        return Paginator<Item>(result.total, result.results)
    }

    private fun buildSimpleSearchPredicate(search: ItemSearch, item: QItem): BooleanBuilder {

        val queryBuilder = BooleanBuilder();

        if (search.name != null) {
            queryBuilder.and( item.name.containsIgnoreCase(search.name) )
        }
        if (search.categoryIds.isNotEmpty()) {
            queryBuilder.and( item.category.id.`in`(search.categoryIds) )
        }
        if (search.cityName != null) {
            queryBuilder.and( item.store.location.city.contains(search.cityName) )
        }
        if(search.storeIds.isNotEmpty()) {
            queryBuilder.and( item.store.id.`in`(search.storeIds) )
        }
        if (search.priceMIn != null && search.priceMIn.toDouble() > 0) {
            queryBuilder.and( item.price.actionPrice.goe(search.priceMIn))
        }
        if (search.priceMax != null && search.priceMax.toDouble() > 0) {
            queryBuilder.and( item.price.actionPrice.loe(search.priceMax))
        }

        var today: LocalDate = LocalDate.now();
        queryBuilder.and(item.price.activeFrom.loe(today))
        queryBuilder.and(item.price.activeTo.goe(today));

        return queryBuilder;
    }

    private fun buildDiscountSearchPredicate(search: ItemSearch, item: QItem): BooleanBuilder {

        val queryBuilder = BooleanBuilder();

        if (search.name != null) {
            queryBuilder.and( item.name.containsIgnoreCase(search.name) )
        }
        if (search.categoryIds.isNotEmpty()) {
            queryBuilder.and( item.category.id.`in`(search.categoryIds) )
        }
        if (search.cityName != null) {
            queryBuilder.and( item.store.location.city.contains(search.cityName) )
        }
        if(search.storeIds.isNotEmpty()) {
            queryBuilder.and( item.store.id.`in`(search.storeIds) )
        }
        if (search.priceMIn != null && search.priceMIn.toDouble() > 0) {
            queryBuilder.and( item.price.actionPrice.goe(search.priceMIn))
        }
        if (search.priceMax != null && search.priceMax.toDouble() > 0) {
            queryBuilder.and( item.price.actionPrice.loe(search.priceMax))
        }

        var today: LocalDate = LocalDate.now();
        queryBuilder.and(item.price.originalPrice.isNotNull);
        queryBuilder.and(item.price.actionPrice.isNotNull);
        queryBuilder.and(item.price.activeFrom.loe(today))
        queryBuilder.and(item.price.activeTo.goe(today));

        return queryBuilder;
    }
}

interface ItemRepository: PagingAndSortingRepository<Item, Long>, SearchItemRepository {
    override fun findAll(): List<Item>;

    override fun findAll(pageable: Pageable): Page<Item>
}
