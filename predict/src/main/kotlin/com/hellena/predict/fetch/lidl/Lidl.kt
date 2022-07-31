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
        // 18.07. - 24.07.
        list.add(Link(Week.THIS_WEEK_MON, "Ostajemo povoljni", "https://www.lidl.hr/c/ostajemo-povoljni/c5483/w1"))
        list.add(Link(Week.THIS_WEEK_MON, "Super svježe od ponedjeljka", "https://www.lidl.hr/c/super-svjeze-od-ponedjeljka/c5484/w1"))
        list.add(Link(Week.THIS_WEEK_MON, "Top brendovi", "https://www.lidl.hr/c/top-brendovi/c5485/w1"))
        list.add(Link(Week.THIS_WEEK_MON, "Tradicionalni proizvodi","https://www.lidl.hr/c/dozivite-sarenilo-i-okuse-svjezih-tradicionalnih-proizvoda/c5486/w1"))
        list.add(Link(Week.THIS_WEEK_MON, "Ljetna kombinacija","https://www.lidl.hr/c/odaberi-svoju-ljetnu-kombinaciju/c5476/w1"))
        list.add(Link(Week.THIS_WEEK_MON, "Vesele boje ljeta","https://www.lidl.hr/c/vesele-boje-ljeta/c5477/w1"))
        list.add(Link(Week.THIS_WEEK_MON, "Kupaonica","https://www.lidl.hr/c/kupaonica-iz-snova/c5478/w1"))
        list.add(Link(Week.THIS_WEEK_MON, "Uredno","https://www.lidl.hr/c/uredno-i-pregledno/c5479/w1"))
        list.add(Link(Week.THIS_WEEK_THU, "Priroda", "https://www.lidl.hr/c/moj-kutak-prirode/c5480/w1"))
        list.add(Link(Week.THIS_WEEK_THU, "Insekti", "https://www.lidl.hr/c/zastitite-se-od-insekata/c5482/w1"))
        list.add(Link(Week.THIS_WEEK_THU, "Svježe od četvrtka", "https://www.lidl.hr/c/super-svjeze-od-cetvrtka/c5510/w1"))
        list.add(Link(Week.THIS_WEEK_THU , "Gril", "https://www.lidl.hr/c/grill-chill-obicaji/c5488/w1"))
        list.add(Link(Week.THIS_WEEK_THU , "Top brendovi", "https://www.lidl.hr/c/top-brendovi/c5489/w1"))
        list.add(Link(Week.THIS_WEEK_THU, "Karibi", "https://www.lidl.hr/c/uzivaj-u-okusima-kariba/c5490/w1"))
        list.add(Link(Week.THIS_WEEK_FRI, "Povoljni petak", "https://www.lidl.hr/c/ostajemo-povoljni-i-vikendom/c5481/w1"))

        // 25.07. - 31.07.
        list.add(Link(Week.NEXT_WEEK_MON, "Ostajemo povoljni", "https://www.lidl.hr/c/ostajemo-povoljni/c5499/w2"))
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
