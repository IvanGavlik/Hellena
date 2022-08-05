package com.hellena.predict.item

import com.hellena.predict.search.Page
import java.math.BigDecimal

data class ItemSearch(
    val name: String?,
    val nameStarsWith:  Boolean?, // TODO refactor
    val categoryIds: List<Long>,
    val storeIds: List<Long>,
    val priceMIn: BigDecimal?,
    val priceMax: BigDecimal?,
    val page: Page
) {
}
