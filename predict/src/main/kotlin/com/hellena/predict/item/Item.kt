package com.hellena.predict.item

import com.hellena.predict.item.category.Category
import com.hellena.predict.item.category.CategoryRepository
import com.hellena.predict.item.price.Price
import com.hellena.predict.item.store.Store
import com.hellena.predict.item.store.StoreRepository
import com.hellena.predict.search.Paginator
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.jpa.impl.JPAQueryFactory
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.expression.common.ExpressionUtils.toLong
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.util.Collections
import java.util.stream.Collectors
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

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
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
    val entityManager: EntityManager,
    val storeRepository: StoreRepository,
    val categoryRepository: CategoryRepository
): SearchItemRepository {

    private val query = JPAQueryFactory(this.entityManager)

    override fun search(search: ItemSearch): Paginator<Item> {
        val item = QItem.item;

        val predicate = buildSimpleSearchPredicate(search, item);

        val result = query.selectFrom(item)
            .where(predicate)
            .orderBy(OrderSpecifier(Order.ASC, item.category.id))
            .offset(search.page.getIndex())
            .limit(search.page.getSize()) // TODO WHAT IF PAGE SIZE IS NOT SET
            .fetchResults()

        val resultWithKonzum = askKonzum(search, result);
        return Paginator<Item>(resultWithKonzum.size, resultWithKonzum.items)
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
            if (search.nameStarsWith != null && search.nameStarsWith) {
                queryBuilder.and( item.name.startsWithIgnoreCase(search.name) )
            } else {
                queryBuilder.and( item.name.containsIgnoreCase(search.name) )
            }
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

    private fun askKonzum(search: ItemSearch, result: QueryResults<Item>): TempQueryResults {
        if (search.name == null || search.page.getIndex() != 0L || result.results.size >= search.page.getSize()) { // TODO IF MORE THEH 12 THEN WE NOT SEE KONZUM DATA
            return TempQueryResults(result.total, result.results);
        }
        val hasKonzum = result.results.filter { ("KONZUM").equals(it.store.name) }
        if (hasKonzum.isNotEmpty()) {
            return TempQueryResults(result.total, result.results);
        }
        // TODO i DON NOT KNOW category so (KRUH && CATEGORY CHILDREN && STORE IS EMPTY) -> REPONSE kRHU IN CATEGORY
        if (search.storeIds.isNotEmpty()) {
            val hasKonzumInSearch = search.storeIds.filter { 5L == it }
            if (hasKonzumInSearch.isEmpty()) { // search by store but there is no Konzum
                return TempQueryResults(result.total, result.results);
            }
        }

        val maxNumber = search.page.getSize() - result.total - 1;
        if (maxNumber <= 0) {
            return TempQueryResults(result.total, result.results);
        }

        val url = "https://www.konzum.hr/web/search?utf8=%E2%9C%93&search%5Bscope%5D=products&filters%5BKategorije%5D%5B%5D=&search%5Bterm%5D=" + search.name;
        val itemKonzum = this.parseFromUrl(url).stream().limit(maxNumber).collect(Collectors.toList()); // TODO display only first page user will not be aware that there is second page
        var total: Long = itemKonzum.size.toLong();
        if (result.results != null) {
            itemKonzum.addAll(result.results)
            total += result.total
        }

        return TempQueryResults(total, itemKonzum);
    }

    private fun parseFromUrl(url:String): List<Item> {
        println(url)
        val konzumStore: Store = storeRepository.findById(5L).get();
        val category: Category = this.categoryRepository.findById(1L).get(); // now is random TODO should category be optional
        val doc = Jsoup.connect(url).get();
        val productListWrapper = doc.select(".product-list");

        return productListWrapper.select(".product-item")
            .stream()
            .map{ toItem(it, konzumStore, category)}
            .filter { it != null }
            .collect(Collectors.toList())
    }

    private fun toItem(element: Element, konzumStore: Store, category: Category): Item {
        val productList: Element = element.select("div[data-ga-name]").first()!!
        val name = productList.attr("data-ga-name")
        val price = productList.attr("data-ga-price")
            .replace('.', ' ')
            .trim()
            .replace(',', '.')
            .replace(" kn", "")
            .trim()
        val imgId = element.select(".link-to-product")
            .select("img")
            .attr("src")

        val itemPrice = Price(
            originalPrice = null, // TODO original price because of daily deal
            actionPrice = BigDecimal(price),
            activeFrom = LocalDate.now().minusDays(-1),
            activeTo = LocalDate.now().plusDays(5), // TODO should change this extract store info
            previous = null,
        )
        return Item(
            name = name,
            longName = name,
            description = name,
            imageId = imgId,
            category = category,
            store = konzumStore,
            price = itemPrice,
            userName = "Hellena",
        )
    }

}

interface ItemRepository: PagingAndSortingRepository<Item, Long>, SearchItemRepository {
    override fun findAll(): List<Item>;

    override fun findAll(pageable: Pageable): Page<Item>
}

data class TempQueryResults(val size: Long, val items: List<Item>) {}
