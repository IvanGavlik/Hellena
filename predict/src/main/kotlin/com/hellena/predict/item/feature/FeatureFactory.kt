package com.hellena.predict.item.feature

import com.hellena.predict.api.model.PageDto
import com.hellena.predict.item.Item
import com.hellena.predict.item.ItemRepository
import com.hellena.predict.item.ItemSearch
import com.hellena.predict.item.feature.ItemFeatureType.*
import com.hellena.predict.search.Page
import com.hellena.predict.search.Paginator
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
    fun fetch(search: ItemSearch): Paginator<Item>;
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
    override fun fetch(search: ItemSearch): Paginator<Item> {

        val pages = itemRepository.searchDiscountExistOrderByBiggestAsc(search)
        val list = pages.elements.stream()
                .sorted { i1, i2 -> i2.discountPercentage!!.toInt()!!.compareTo(i1.discountPercentage!!.toInt()) } // TODO MOVE TO SQLs
                .collect(Collectors.toList());

        return Paginator(pages.size, list);
    }
}

@Component
class CheapestFirstItemFeature: ItemFeature {
    override fun fetch(search: ItemSearch): Paginator<Item> {
        println("CheapestFirstItemFeature");
        TODO("Not yet implemented")
    }
}

