package com.hellena.predict.item

import com.hellena.predict.item.QItem.Companion.item
import com.hellena.predict.item.category.Category
import com.hellena.predict.item.price.Price
import com.hellena.predict.item.store.Store
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import java.math.BigDecimal
import java.util.function.Predicate
import java.util.stream.Collectors.toList
import javax.persistence.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
import kotlin.math.min

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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    val category: Category,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "item_store",
        joinColumns = [JoinColumn(name = "item_id", )],
        inverseJoinColumns = [JoinColumn(name ="store_id", referencedColumnName = "id")]
    )
    val store: List<Store>,

    @OneToOne(fetch = FetchType.EAGER)
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

        query.select(root);

        // TODO sql for this (query)
        var skip = 0L;
        if (search.page.getIndex() > 0) {
            skip = search.page.getIndex() * search.page.getSize()
        }


        return entityManager.createQuery(query).resultList.stream()
            .filter { filterByName(it, search.name) }
            .filter { filterByCity(it, search.cityName) }
            .filter { filterByCategory(it, search.categoryIds) }
            .filter { filterByStore(it, search.storeIds) }
            .filter { filterByPriceMIn(it, search.priceMIn) }
            .filter { filterByPriceMax(it, search.priceMIn) }
            .skip(skip)
            .limit(search.page.getSize())
            .collect(toList());
    }

    private fun filterByName(item: Item, name: String?) : Boolean {
        var byName = true;
        if (name != null) {
            byName = name?.contains(name);
        }
        return byName;
    }

    private fun filterByCity(item: Item, city: String?) : Boolean {
        var byCity = true;
        if(city != null) {
            byCity = item.store.stream()
                .filter( { it.location.city.equals(city)  } )
                .findFirst()
                .isPresent
        }
        return byCity;
    }

    private fun filterByCategory(item: Item, categoryIds: List<Long>) : Boolean {
        var byCat = true;
        if(categoryIds.isNotEmpty()) {
            byCat = categoryIds.stream()
                .filter({ it == item.category.id })
                .findFirst()
                .isPresent;
        }
        return byCat;
    }

    private fun filterByStore(item: Item, storeIds: List<Long>) : Boolean {
        var byStore = true;
        if(storeIds.isNotEmpty()) {
            byStore = false;
            item.store.forEach {
                if (storeIds.contains(it.id)) {
                    byStore = true;
                }
            }
        }
        return byStore;
    }

    private fun filterByPriceMIn(item: Item, minPrice: BigDecimal?) : Boolean {
        var price = true;
        if (minPrice != null) {
            price = item.price.actionPrice.equals(minPrice);
        }
        return price;
    }

    private fun filterByPriceMax(item: Item, maxPrice: BigDecimal?) : Boolean {
        var price = true;
        if (maxPrice != null) {
            price = item.price.actionPrice.equals(maxPrice);
        }
        return price;
    }
}

interface ItemRepository: PagingAndSortingRepository<Item, Long>, SearchItemRepository {
    override fun findAll(): List<Item>;

    override fun findAll(pageable: Pageable): Page<Item>
}

