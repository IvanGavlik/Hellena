package com.hellena.predict.fetch.lidl

import com.hellena.predict.fetch.Fetch
import com.hellena.predict.item.Item
import com.hellena.predict.item.category.Category
import com.hellena.predict.item.price.Price
import com.hellena.predict.item.store.Store
import com.hellena.predict.item.store.StoreRepository
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.stream.Collectors

class Lidl(val storeRepository: StoreRepository, val category: Category): Fetch {
    val lidlStore: Store = storeRepository.findById(4L).get();

    override fun fetch(): List<Item> {
        return getLinks().stream()
            .flatMap {  parseFromUrl(it).stream() }
            .collect(Collectors.toList())
    }

    private fun parseFromUrl(link: Link): List<Item> {
        val doc = Jsoup.connect(link.link).get();

        val productListWrapper = doc.select(".ret-o-card");

        return productListWrapper.stream()
            .map { toItem(it, link) }
            .collect(Collectors.toList())
    }

    private fun toItem(element: Element, link: Link): Item {

        val originalPrice = element
            .select(".lidl-m-pricebox__discount-price")
            .first()
            ?.text()

        val price = element
            .select(".lidl-m-pricebox__price")
            .first()!!
            .text()

        val activeFrom = activeFromWeek(link.week);

        val itemPrice = Price(
            originalPrice = priceFromString(originalPrice), // TODO original price because of daily deal
            actionPrice = priceFromString(price)!!,
            activeFrom = activeFrom,
            activeTo = activeFrom.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)), // TODO should change this extract store info
        )

        val name = element
            .select(".ret-o-card__headline")
            .first()!!
            .text()

        val imgId = element.select("img")
            .attr("src");

        return Item(
            name = name,
            imageId = imgId,
            category = category,
            store = lidlStore,
            price = itemPrice,
            userName = "Hellena",
        )
    };

    private fun activeFromWeek(week: Week): LocalDate {
        return when(week) {
            Week.THIS_WEEK_MON -> LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            Week.THIS_WEEK_THU -> LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.THURSDAY))
            Week.THIS_WEEK_FRI -> LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.FRIDAY))
            Week.NEXT_WEEK_MON -> LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))
            Week.NEXT_WEEK_THU -> LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY))
            Week.NEXT_WEEK_FRI -> LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY))
        }
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

    private fun getLinks(): List<Link> {
        val list = mutableListOf<Link>()
        // 01.08. - 07.08.
        list.add(Link(Week.THIS_WEEK_MON, "Ostajemo povoljni", "https://www.lidl.hr/c/ostajemo-povoljni/c5511/w1"))
        list.add(Link(Week.THIS_WEEK_MON, "Svježe od ponedjeljka", "https://www.lidl.hr/c/super-svjeze-od-ponedjeljka/c5512/w1"))
        list.add(Link(Week.THIS_WEEK_MON, "Ostajemo povoljni 2", "https://www.lidl.hr/c/ostajemo-povoljni/c5513/w1"))
        list.add(Link(Week.THIS_WEEK_MON, "Okusi Grčke", "https://www.lidl.hr/c/uzivaj-u-okusima-grcke/c5514/w1"))
        list.add(Link(Week.THIS_WEEK_MON, "Okusi Provanse", "https://www.lidl.hr/c/uzivaj-u-okusima-provanse/c5515/w1"))
        list.add(Link(Week.THIS_WEEK_MON, "Ležeran stil", "https://www.lidl.hr/c/moj-lezeran-ljetni-stil/c5522/w1"))
        list.add(Link(Week.THIS_WEEK_MON, "Kuhinja", "https://www.lidl.hr/c/opremite-svoju-kuhinju/c5523/w1"))
        list.add(Link(Week.THIS_WEEK_THU, "Odmor i zabava", "https://www.lidl.hr/c/za-odmor-i-zabavu/c5524/w1"))
        list.add(Link(Week.THIS_WEEK_THU, "Back to school", "https://www.lidl.hr/c/back-to-school/c5525/w1"))
        list.add(Link(Week.THIS_WEEK_THU, "Gril", "https://www.lidl.hr/c/grill-chill-obicaji/c5517/w1"))
        list.add(Link(Week.THIS_WEEK_THU, "Sve po", "https://www.lidl.hr/c/sve-po-5-7-ili-9-kn/c5518/w1"))
        list.add(Link(Week.THIS_WEEK_THU, "Okusi engleske", "https://www.lidl.hr/c/uzivaj-u-okusima-velike-britanije-i-irske/c5519/w1"))
        list.add(Link(Week.THIS_WEEK_THU, "Craft", "https://www.lidl.hr/c/ready-for-craft/c5520/w1"))
        list.add(Link(Week.THIS_WEEK_FRI, "Vikend", "https://www.lidl.hr/c/ostajemo-povoljni-i-vikendom/c5521/w1"))


        // 25.07. - 31.07.
/*        list.add(Link(Week.NEXT_WEEK_MON, "Ostajemo povoljni", "https://www.lidl.hr/c/ostajemo-povoljni/c5499/w2"))
        list.add(Link(Week.NEXT_WEEK_MON, "Super svježe od ponedjeljka", "https://www.lidl.hr/c/super-svjeze-od-ponedjeljka/c5500/w2"))
        list.add(Link(Week.NEXT_WEEK_MON, "Super ušteda", "https://www.lidl.hr/c/ugrabi-super-ustedu/c5501/w2"))
        list.add(Link(Week.NEXT_WEEK_MON, "Zaštita i njega za obitelj", "https://www.lidl.hr/c/zastita-i-njega-za-cijelu-obitelj/c5502/w2"))
        list.add(Link(Week.NEXT_WEEK_MON, "Slatka ljetna osvježenja", "https://www.lidl.hr/c/slatka-ljetna-osvjezenja/c5503/w2"))
        list.add(Link(Week.NEXT_WEEK_MON, "Francuska", "https://www.lidl.hr/c/uzivaj-u-okusima-francuske/c5505/w2"))
        list.add(Link(Week.NEXT_WEEK_MON, "Sicilija", "https://www.lidl.hr/c/uzivaj-u-okusima-sicilije/c5506/w2"))
        list.add(Link(Week.NEXT_WEEK_MON, "Dobar san", "https://www.lidl.hr/c/dobar-san-za-sretniji-dan/c5492/w2"))
        list.add(Link(Week.NEXT_WEEK_MON, "Top forma", "https://www.lidl.hr/c/do-top-forme-uz-crivit/c5493/w2"))
        list.add(Link(Week.NEXT_WEEK_THU, "Moda ljetna", "https://www.lidl.hr/c/moda-inspirirana-ljetom/c5494/w2"))
        list.add(Link(Week.NEXT_WEEK_THU, "Mališani", "https://www.lidl.hr/c/za-vase-malisane/c5495/w2"))
        list.add(Link(Week.NEXT_WEEK_THU, "Ti to možeš", "https://www.lidl.hr/c/ti-to-mozes/c5496/w2"))
        list.add(Link(Week.NEXT_WEEK_THU, "Dodatci za dom", "https://www.lidl.hr/c/dodatci-za-svaki-dom/c5497/w2"))
        list.add(Link(Week.NEXT_WEEK_THU, "Gril", "https://www.lidl.hr/c/grill-chill-obicaji/c5507/w2"))
        list.add(Link(Week.NEXT_WEEK_THU, "Osviještena prehrana", "https://www.lidl.hr/c/osvijestena-prehrana-za-dobar-osjecaj/c5508/w2"))
        list.add(Link(Week.NEXT_WEEK_THU, "Veganski proizvodi", "https://www.lidl.hr/c/osvijestena-prehrana-za-dobar-osjecaj/c5508/w2"))
        list.add(Link(Week.NEXT_WEEK_FRI, "Povoljni petak", "https://www.lidl.hr/c/ostajemo-povoljni-i-vikendom/c5498/w2"))

 */
        return list;
    }
}

enum class Week {
    THIS_WEEK_MON,
    THIS_WEEK_THU,
    THIS_WEEK_FRI,
    NEXT_WEEK_MON,
    NEXT_WEEK_THU,
    NEXT_WEEK_FRI,
}

data class Link(val week: Week, val info: String, val link: String) {}
