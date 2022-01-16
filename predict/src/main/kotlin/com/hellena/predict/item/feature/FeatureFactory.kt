package com.hellena.predict.item.feature

import com.hellena.predict.item.Item
import com.hellena.predict.item.ItemRepository
import com.hellena.predict.item.ItemSearch
import com.hellena.predict.item.feature.ItemFeatureType.*
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component


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
        // TODO Implement
        return itemRepository.search(search);
    }
}

@Component
class CheapestFirstItemFeature: ItemFeature {
    override fun fetch(search: ItemSearch): List<Item> {
        println("CheapestFirstItemFeature");
        TODO("Not yet implemented")
    }
}

