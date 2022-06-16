package com.hellena.predict.fetch.plodine

import com.hellena.predict.fetch.Fetch
import com.hellena.predict.item.Item
import com.hellena.predict.item.category.Category
import com.hellena.predict.item.category.CategoryRepository
import com.hellena.predict.item.price.Price
import com.hellena.predict.item.store.StoreRepository
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.util.stream.Collectors

/**
 * TODO
 * -> Vikend akcija https://www.plodine.hr/akcije/10/vikend-akcija
 * -> Početak tjedna https://www.plodine.hr/akcije/11/pocetak-tjedna
 * -> XXL tjedan https://www.plodine.hr/akcije/47/xxl-tjedan
 *
 * TODO
 * -> pošto je tjedna akcije mozda samo 1 tjedno pokrenuti u pon
 * -> end and start date set (same in Konzum)
 */
@Service
class Plodine(val storeRepository: StoreRepository, val categoryRepository: CategoryRepository): Fetch {

    val plodineStore = storeRepository.findById(1).get();
    val urls = mutableMapOf<Category, List<String>>();

    init {
        val meatAndFish = this.categoryRepository.findById(5).get()
        urls.put(meatAndFish,
            listOf(
                "https://www.plodine.hr/akcije/13/tjedna-ponuda/mesnica-i-ribarnica",
                "https://www.plodine.hr/akcije/26/tjedna-ponuda/grill"
            )
        )

        val milkProducts = this.categoryRepository.findById(4).get()
        urls.put(milkProducts,
            listOf(
                "https://www.plodine.hr/akcije/14/tjedna-ponuda/mlijecni-proizvodi",
                "https://www.plodine.hr/akcije/15/tjedna-ponuda/sirevi"
            )
        )

        val frozen = this.categoryRepository.findById(10).get()
        urls.put(frozen, listOf("https://www.plodine.hr/akcije/16/tjedna-ponuda/smrznuto"))

        val delicatessen = this.categoryRepository.findById(6).get();
        urls.put(delicatessen, listOf("https://www.plodine.hr/akcije/17/tjedna-ponuda/suhomesnati-proizvodi"))

        val fruit = this.categoryRepository.findById(3).get();
        urls.put(fruit, listOf("https://www.plodine.hr/akcije/21/tjedna-ponuda/voce-i-povrce"))

        val bakery = this.categoryRepository.findById(8).get();
        urls.put(bakery, listOf("https://www.plodine.hr/akcije/22/tjedna-ponuda/pekarnica"))

        val flakesSpreadsCoffeesTeas = this.categoryRepository.findById(17).get();
        urls.put(flakesSpreadsCoffeesTeas, listOf("https://www.plodine.hr/akcije/23/tjedna-ponuda/prehrana-kava-i-caj"))

        val drinks = this.categoryRepository.findById(7).get();
        urls.put(drinks, listOf("https://www.plodine.hr/akcije/24/tjedna-ponuda/pice"))

        val sweets = this.categoryRepository.findById(9).get();
        urls.put(sweets, listOf("https://www.plodine.hr/akcije/25/tjedna-ponuda/slatkisi-i-grickalice"))

        val kitchen = this.categoryRepository.findById(21).get();
        urls.put(kitchen, listOf("https://www.plodine.hr/akcije/28/tjedna-ponuda/kuhinja"))

        val cleaning = this.categoryRepository.findById(19).get();
        urls.put(cleaning,
            listOf(
                "https://www.plodine.hr/akcije/33/tjedna-ponuda/drogerija",
                "https://www.plodine.hr/akcije/38/tjedna-ponuda/ciscenje-i-organizacija"
            )
        )

        val pets = this.categoryRepository.findById(18).get();
        urls.put(pets, listOf("https://www.plodine.hr/akcije/34/tjedna-ponuda/kucni-ljubimci"))

        val cars = this.categoryRepository.findById(24).get();
        urls.put(cars, listOf("https://www.plodine.hr/akcije/40/tjedna-ponuda/auto-oprema-i-tehnika"))

    }

    override fun fetch(): List<Item> {
        var items = mutableListOf<Item>()
        urls.forEach{url ->
            val itemsUrl = url.value.stream()
                .map{ urlToItems(url.key, it) }
                .flatMap { it.stream() }
                .collect(Collectors.toList())
            items.addAll(itemsUrl)
        }
        return items;
    }

    private fun urlToItems(category: Category, url: String): List<Item> {
        println(category.name + " " + url)
        val doc = Jsoup.connect(url).get();
        val productListWrapper = doc.select(".category__item");

        return productListWrapper.stream()
            .map{ this.elementToItem(category, it) }
            .collect(Collectors.toList())
    }

    private fun elementToItem(category: Category, element: Element): Item {

        val img = element.select(".card__img").first()?.attr("src")
        val itemName = element.select(".card__title").first()?.text().toString();
        var price = element.select(".card__price").select(".regular").select("strong").text().toString()
            .replace('.', ' ')
            .trim()
            .replace(',', '.')
            .trim()

        if("".equals(price)) {
            price = "0";
        }

        return Item(
            name = itemName,
            longName = itemName,
            description = itemName,
            price = Price(null, BigDecimal(price), LocalDate.now().minusDays(1), LocalDate.now().plusDays(7), null),
            category = category,
            imageId = img,
            store = this.plodineStore,
            userName = "Hellena"
        )
    }

}

