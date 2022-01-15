package com.hellena.predict.item.feature

import com.hellena.predict.item.Item
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component


@Component
class FeatureFactory {

    fun getFeature(type: String): ItemFeature {
        return CheapestFirstItemFeature();
    }
}


interface ItemFeature {
    fun fetch(): List<Item>;
}

@Component
class CheapestTodayItemFeature: ItemFeature {
    override fun fetch(): List<Item> {
        println("CheapestTodayItemFeature");
        TODO("Not yet implemented")
    }
}

@Component
class CheapestFirstItemFeature: ItemFeature {
    override fun fetch(): List<Item> {
        println("CheapestFirstItemFeature");
        TODO("Not yet implemented")
    }
}

