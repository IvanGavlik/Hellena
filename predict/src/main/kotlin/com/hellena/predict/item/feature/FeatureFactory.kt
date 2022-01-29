package com.hellena.predict.item.feature

import com.hellena.predict.api.model.PageDto
import com.hellena.predict.item.Item
import com.hellena.predict.item.ItemRepository
import com.hellena.predict.item.ItemSearch
import com.hellena.predict.item.feature.ItemFeatureType.*
import com.hellena.predict.search.Page
import com.hellena.predict.search.Sort
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.stream.Collectors


@Component
class FeatureFactory(val app: ApplicationContext) {

    fun getFeature(type: ItemFeatureType?): ItemFeature? {
        return when (type) {
            CHEAPEST_TODAY -> this.app.getBean(CheapestTodayItemFeature::class.java)
            else -> {
                null
            }
        }
    }
}


interface ItemFeature {
    fun fetch(search: ItemSearch): List<Item>;
}

enum class ItemFeatureType {
    CHEAPEST_TODAY,
    CHEAPEST_FIRST,
    CHEAPEST_LAST,
    GREATER_SAVINGS_FIRST,
    GREATER_SAVINGS_LAST,
}

@Component
class CheapestTodayItemFeature(val itemRepository: ItemRepository): ItemFeature {
    override fun fetch(search: ItemSearch): List<Item> {

       val searchAll = ItemSearch(
           name = search.name,
           categoryIds =  search.categoryIds,
           cityName = search.cityName,
           storeIds = search.storeIds,
           priceMIn = search.priceMIn,
           priceMax = search.priceMax,
           page = getAll()
       );

        return itemRepository.search(searchAll).stream()
            .filter { it.discountPercentage != null }
            .skip(search.page.getIndex())
            .limit(search.page.getSize())
            .sorted { i1, i2 -> i2.discountPercentage!!.toInt()!!.compareTo(i1.discountPercentage!!.toInt()) }
            .collect(Collectors.toList());
    }

    fun getAll(): Page {
        return object: Page {
            override fun getIndex(): Long {
                return 0;
            }
            override fun getSize(): Long {
                return 1200; // TODO WHAT IF PAGE SIZE IS NOT SET
            }
            override fun getSort(): List<Sort> {
                return emptyList();
            }
        }
    }
}

@Component
class CheapestFirstItemFeature: ItemFeature {
    override fun fetch(search: ItemSearch): List<Item> {
        println("CheapestFirstItemFeature");
        TODO("Not yet implemented")
    }
}

