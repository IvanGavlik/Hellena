package com.hellena.predict.fetch.kaufland

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

class Kaufland(val categories: List<Category>, val storeRepository: StoreRepository): Fetch {

    val kauflandStore: Store = storeRepository.findById(3L).get();

    override fun fetch(): List<Item> {
        return this.categories.stream()
            .map { this.fetchItemsByCategory(it) }
            .flatMap { it.stream() }
            .collect(Collectors.toList())
    }

    private fun fetchItemsByCategory(category: Category): List<Item> {
        return getLinksByCategoryName(category.name).stream()
            .map{ this.parseFromUrl(it, category) }
            .flatMap { it.stream() }
            .collect(Collectors.toList())
    }

    // TODO KAUFLAND CARD POPUST
    // TOTO original price
    private fun getLinksByCategoryName(name: String): List<String> {
        if("Voće i povrće".contentEquals(name)) {
            return listOf(
                "https://www.kaufland.hr/ponuda/ponuda-od-cetvrtka/ponuda-pregled.category=01a_Vo%C4%87e__povr%C4%87e__biljke.html",
            )
        }

        if("Meso i riba".contentEquals(name)) {
            return listOf(
                "https://www.kaufland.hr/ponuda/ponuda-od-cetvrtka/ponuda-pregled.category=01_Meso__perad__kobasice.html",
                "https://www.kaufland.hr/ponuda/ponuda-od-cetvrtka/ponuda-pregled.category=02_Svje%C5%BEa_riba.html"
            )
        }

        if("Delikatesa".contentEquals(name)) {
            return listOf(
                "https://www.kaufland.hr/ponuda/ponuda-od-cetvrtka/ponuda-pregled.category=05_Delikatese__konzerve.html",
            )
        }

        if("Pića".contentEquals(name)) {
            return listOf(
                "https://www.kaufland.hr/ponuda/ponuda-od-cetvrtka/ponuda-pregled.category=08_Napitci__alkoholna_pi%C4%87a.html",)
        }

        if("Smrznuta hrana".contentEquals(name)) {
            return listOf(
                "https://www.kaufland.hr/ponuda/ponuda-od-cetvrtka/ponuda-pregled.category=04_Smrznuti_proizvodi.html"
            )
        }

        if("Mliječni proizvodi i jaja".contentEquals(name)) {
            return listOf(
                "https://www.kaufland.hr/ponuda/ponuda-od-cetvrtka/ponuda-pregled.category=03_Mlije%C4%8Dni_proizvodi.html",
            )
        }

        if("Pekarnica".contentEquals(name)) {
            return listOf()
        }

        if("Slatkiši i grickalice".contentEquals(name)) {
            return listOf(
                "https://www.kaufland.hr/ponuda/ponuda-od-cetvrtka/ponuda-pregled.category=07_Kava__%C4%8Daj__slatki%C5%A1i__grickalice.html",
            )
        }

        if("Priprema jela".contentEquals(name)) {
            return listOf("https://www.kaufland.hr/ponuda/ponuda-od-cetvrtka/ponuda-pregled.category=06_Osnovne_%C5%BEive%C5%BEne_namirnice.html")
        }

        if("Tjestenina, riža, njoki, tortilje".contentEquals(name)) {
            return listOf( )
        }

        if("Umaci i začini".contentEquals(name)) {
            return listOf()
        }

        if("Brzi obrok".contentEquals(name)) {
            return listOf()
        }

        if("Konzervirano i juhe".contentEquals(name)) {
            return listOf()
        }

        if("Priprema kolača".contentEquals(name)) {
            return listOf()
        }

        if("Pahuljice, namazi, kave, čajevi".contentEquals(name)) {
            return listOf()
        }

        if("Kućni ljubimci".contentEquals(name)) {
            return listOf()
        }

        if("Čišćenje i pospremanje".contentEquals(name)) {
            return listOf(
                "https://www.kaufland.hr/ponuda/ponuda-od-cetvrtka/ponuda-pregled.category=09_Drogerija__hrana_za_ku%C4%87ne_ljubimce.html"
            )
        }

        if("Njega i higijena".contentEquals(name)) {
            return listOf()
        }

        if("Kućanske potrepštine".contentEquals(name)) {
            return listOf(
                "https://www.kaufland.hr/ponuda/ponuda-od-cetvrtka/ponuda-pregled.category=10_Elektroure%C4%91aji__uredski_pribor__multimedija.html",
                "https://www.kaufland.hr/ponuda/ponuda-od-cetvrtka/ponuda-pregled.category=11_Sve_za_ku%C4%87anstvo.html",
            )
        }

        if("Auto program".contentEquals(name)) {
            return listOf(
                "https://www.kaufland.hr/ponuda/ponuda-od-cetvrtka/ponuda-pregled.category=12_Odje%C4%87a__oprema_za_automobil__slobodno_vrijeme__igra%C4%8Dke.html"
            )
        }

        /*
        TODO
        Pazim što jedem
        Posuđe
        Party
        Škola i ured

        */

        // TODO KOLAĆI I TORTE https://www.konzum.hr/web/t/kategorije/pekarnica/kolaci-i-torte
        // TODO smrtnuta hrana https://www.konzum.hr/web/t/kategorije/voce-i-povrce/smrznuto-voce-i-povrce

        return emptyList();
    }

    private fun parseFromUrl(url:String, category: Category): List<Item> {
        if (url.isEmpty()) {
            return emptyList();
        }
        println(url)
        val doc = Jsoup.connect(url).get();

        val productListWrapper = doc.select(".o-overview-list__item-inner");
        return productListWrapper.stream()
            .map { this.elementToItem(it, category) }
            .filter { it != null }
            .collect(Collectors.toList()) as List<Item>
    }

    private fun elementToItem(element: Element, category: Category): Item? {
        val imgId = element.select(".m-figure")
            .select("img")
            .attr("data-src")

        val subtitle = element.select(".m-offer-tile__text").select(".m-offer-tile__subtitle").text();
        val title = element.select(".m-offer-tile__text").select(".m-offer-tile__title").text();
        val quantity = element.select(".m-offer-tile__text").select(".m-offer-tile__quantity").text();
        val name = subtitle + " " + title + " " + quantity;
        if (name.trim().isEmpty() || name.equals("  ")) {
            return null;
        }

        val price = element
            .select(".a-pricetag__price-container ")
            .first()!!
            .select(".a-pricetag__price")
            .text()
            .replace(" kn", "")
            .replace("-", "00") // TODO MAY BE KAUFLAND CARD
            .trim();

        val start = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.THURSDAY));
        val itemPrice = Price(
            originalPrice = null, // TODO original price because of daily deal
            actionPrice = BigDecimal(price),
            activeFrom = start,
            activeTo = start.plusDays(5), // TODO should change this extract store info
            previous = null,
        )
        return Item(
            name = name,
            longName = name,
            description = name,
            imageId = imgId,
            category = category,
            store = kauflandStore,
            price = itemPrice,
            userName = "Hellena",
        )
    }

}
