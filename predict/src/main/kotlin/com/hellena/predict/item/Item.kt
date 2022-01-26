package com.hellena.predict.item

import com.hellena.predict.item.category.Category
import com.hellena.predict.item.price.Price
import com.hellena.predict.item.store.Store
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import javax.persistence.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "item_store",
        joinColumns = [JoinColumn(name = "item_id", )],
        inverseJoinColumns = [JoinColumn(name ="store_id", referencedColumnName = "id")]
    )
    val store: List<Store>,

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

    override fun search(search: ItemSearch): List<Item> {
        val builder: CriteriaBuilder = entityManager.criteriaBuilder
        val query: CriteriaQuery<Item> = builder.createQuery(Item::class.java)
        val root: Root<Item> = query.from(Item::class.java)

        query.select(root)
            .where(
                builder.equal(root.get<String>("name"), "test"),
            )

        return entityManager.createQuery(query).resultList;
    }

    /**

    val queryBuilder = BooleanBuilder();

    var today: LocalDate = LocalDate.now();
    queryBuilder.and(item.price.activeFrom.loe(today))
    queryBuilder.and(item.price.activeTo.goe(today));

    return queryBuilder;


     */
    private fun setUpPredicate(builder: CriteriaBuilder,root: Root<Item>, search: ItemSearch) {
        if (search.name != null) {
            builder.and( builder.equal(root.get<String>("name"), search.name) )
        }
        if (search.categoryIds.isNotEmpty()) {
            root.get<Category>("category").`in`(search.categoryIds);
        }
        if (search.cityName != null && search.cityName.isEmpty()) {
            //            queryBuilder.and( item.store..id.`in` (search.cityIds))
        }
        if(search.storeIds.isNotEmpty()) {
            //            queryBuilder.and( item.store contains(search.storeIds) )
        }
        if (search.priceMIn != null && search.priceMIn.toDouble() > 0) {
            queryBuilder.and( item.price.actionPrice.goe(search.priceMIn))
        }
        if (search.priceMax != null && search.priceMax.toDouble() > 0) {
            queryBuilder.and( item.price.actionPrice.loe(search.priceMax))
        }


    }
}

interface ItemRepository: PagingAndSortingRepository<Item, Long>, SearchItemRepository {
    override fun findAll(): List<Item>;

    override fun findAll(pageable: Pageable): Page<Item>
}

