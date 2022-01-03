package com.hellena.predict.item

import com.hellena.predict.search.Page
import java.math.BigDecimal

data class ItemSearch (
    val name: String?,
    val categoryId: Long?,
    val cityId: Long?,
    val priceMIn: BigDecimal?,
    val priceMax: BigDecimal?,
    val page: Page?
) {
}
