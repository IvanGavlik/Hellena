package com.hellena.predict.fetch.interspar

import com.hellena.predict.fetch.Fetch
import com.hellena.predict.item.Item
import com.hellena.predict.item.category.Category
import com.hellena.predict.item.price.Price
import com.hellena.predict.item.store.Store
import com.hellena.predict.item.store.StoreRepository
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.math.BigDecimal
import java.time.LocalDate
import java.util.stream.Collectors

class Interspar(val storeRepository: StoreRepository, val category: Category): Fetch {
    val intersparStore: Store = storeRepository.findById(2L).get();
    override fun fetch(): List<Item> {
        return getLinks().stream()
            .flatMap {  parseFromUrl(it).stream() }
            .collect(Collectors.toList())
    }

    private fun getLinks(): List<String> {
        return listOf(
            "https://www.spar.hr/aktualne-ponude/interspar-ponuda"
        )
    }

    private fun parseFromUrl(link: String): List<Item> {
        println(link);
        val doc = Jsoup.connect(link).get();

        val productListWrapper = doc.select(".product");

        return productListWrapper.stream()
            .map { toItem(it) }
            .collect(Collectors.toList())
    }

    private fun toItem(element: Element): Item {

        val price = element
            .select(".price-tag__price-main")
            .first()!!
            .text()

        val activeFrom = LocalDate.now();

        val itemPrice = Price(
            originalPrice = null, // TODO original price because of daily deal
            actionPrice = priceFromString(price)!!,
            //activeFrom = activeFrom,
            //activeTo = activeFrom.plusDays(6),
            activeFrom = LocalDate.now().minusDays(-3),
            activeTo = LocalDate.now().plusDays(2),
        )

        val name = element
            .select(".tile-basic__headline--product-slim")
            .first()!!
            .text()

        val nameInfo = element.select(".tile-basic__text")
            .first()!!
            .text()

        val imgId = "https://www.spar.hr/" + element.select(".tile-basic__image")
            .attr("src");

        return Item(
            name = "$name $nameInfo",
            imageId = imgId,
            category = category,
            store = intersparStore,
            price = itemPrice,
            userName = "Hellena",
        )
    }

    private fun priceFromString(value: String?): BigDecimal? {
        if (value == null || value.isEmpty() || value == "null") {
            return null;
        }

        val formated = value.trim()
            .replace(',', '.')
            .trim()
            .replace(" kn", "")
            .trim()
            .replace("-", "")
            .trim()

        return BigDecimal(formated)
    }

}
