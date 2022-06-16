package com.hellena.predict.fetch.konzum

import com.hellena.predict.fetch.Fetch
import com.hellena.predict.item.Item
import com.hellena.predict.item.category.Category
import com.hellena.predict.item.price.Price
import com.hellena.predict.item.service.ItemService
import com.hellena.predict.item.store.Store
import com.hellena.predict.item.store.StoreRepository
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.util.stream.Collectors

/**
 * from date today -1
 * end date today + 5
 */
@Service
class Konzum(val categories: List<Category>, val storeRepository: StoreRepository): Fetch {

    val konzumStore: Store = storeRepository.findById(5L).get();

    override fun fetch(): List<Item> {
        return categories.stream()
            .map{ this.fetchItemsByCategory(it) }
            .flatMap { it.stream() }
            .collect(Collectors.toList())
    }

    private fun fetchItemsByCategory(category: Category): List<Item> {
        return getLinksByCategoryName(category.name).stream()
            .map{ this.parseFromUrl(it, category) }
            .flatMap { it.stream() }
            .collect(Collectors.toList())
    }


    private fun getLinksByCategoryName(name: String): List<String> {
        if ("Dječji svijet".contentEquals(name)) {
            return listOf(
                "https://www.konzum.hr/web/t/kategorije/djecji-svijet/pelene-i-vlazne-maramice?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/djecji-svijet/djecja-hrana?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/djecji-svijet/djecja-njega-i-higijena?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/djecji-svijet/deterdzenti-i-omeksivaci?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/djecji-svijet/bocice-dude-pribor?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/djecji-svijet/za-igru?sort[]=&per_page=100"
            );
        }

        if("Voće i povrće".contentEquals(name)) {
            return listOf(
                "https://www.konzum.hr/web/t/kategorije/voce-i-povrce/voce?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/voce-i-povrce/povrce?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/voce-i-povrce/suho-voce-i-povrce-orasasto?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/voce-i-povrce/zacinsko-bilje?sort[]=&per_page=100"
            )
        }

        if("Meso i riba".contentEquals(name)) {
            return listOf(
                "https://www.konzum.hr/web/t/kategorije/meso-i-riba/piletina-i-puretina?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/meso-i-riba/svinjetina-junetina-ostalo?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/riba?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/meso-i-riba/spremno-za-rostilj?sort[]=&per_page=100"
            )
        }

        if("Delikatesa".contentEquals(name)) {
            return listOf(
                "https://www.konzum.hr/web/t/kategorije/delikatesa/mesna-delikatesa?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/delikatesa/hrenovke-i-kobasice?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/delikatesa/sirevi?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/delikatesa/specijaliteti?sort[]=&per_page=100"
            )
        }

        if("Pića".contentEquals(name)) {
            return listOf(
                "https://www.konzum.hr/web/t/kategorije/pica/bezalkoholna/voda?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/pica/bezalkoholna/sokovi-i-ledeni-cajevi?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/pica/bezalkoholna/energetska-pica?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/pica/pivo?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/pica/vino?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/pica/alkoholna-pica?sort[]=&per_page=100"
            )
        }

        if("Smrznuta hrana".contentEquals(name)) {
            return listOf(
                "https://www.konzum.hr/web/t/kategorije/smrznuta-hrana/voce-i-povrce?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/smrznuta-hrana/riba-i-plodovi-mora?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/smrznuta-hrana/tijesto-i-pripravci-od-tijesta?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/smrznuta-hrana/jela-od-mesa-i-povrca?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/smrznuta-hrana/pizze?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/smrznuta-hrana/sladoledi-i-deserti?sort[]=&per_page=100"
            )
        }

        if("Mliječni proizvodi i jaja".contentEquals(name)) {
            return listOf(
                "https://www.konzum.hr/web/t/kategorije/mlijecni-proizvodi-i-jaja/mlijeko?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/mlijecni-proizvodi-i-jaja/sirevi?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/mlijecni-proizvodi-i-jaja/jaja?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/mlijecni-proizvodi-i-jaja/jogurti-i-ostalo?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/mlijecni-proizvodi-i-jaja/vrhnje?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/mlijecni-proizvodi-i-jaja/mlijecni-deserti?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/mlijecni-proizvodi-i-jaja/margarin-maslac-mast?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/mlijecni-proizvodi-i-jaja/namazi?sort[]=&per_page=100",
            )
        }

        if("Pekarnica".contentEquals(name)) {
            return listOf(
                "https://www.konzum.hr/web/t/kategorije/pekarnica/svjezi-kruh?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/pekarnica/pakirani-kruh?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/pekarnica/dvopek-i-toast?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/pekarnica/tijesta-i-kvasci?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/pekarnica/mlinci-i-mrvice?sort[]=&per_page=100",
            )
        }

        if("Slatkiši i grickalice".contentEquals(name)) {
            return listOf(
                "https://www.konzum.hr/web/t/kategorije/slatkisi-i-grickalice/keksi?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/slatkisi-i-grickalice/cokolade?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/slatkisi-i-grickalice/grickalice/stapici-pereci-krekeri-kokice?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/slatkisi-i-grickalice/grickalice/cips?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/slatkisi-i-grickalice/grickalice/flips?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/slatkisi-i-grickalice/grickalice/kikiriki-i-orasasto?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/slatkisi-i-grickalice/bombonijere?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/slatkisi-i-grickalice/bomboni-lizalice-zvakace-gume?sort[]=&per_page=100"
            )
        }

        if("Priprema jela".contentEquals(name)) {
            return listOf(
                "https://www.konzum.hr/web/t/kategorije/priprema-jela/ulja?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/priprema-jela/secer-i-umjetna-sladila?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/priprema-jela/brasno?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/priprema-jela/ocat-i-dresinzi?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/priprema-jela/kase-i-krupice?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/priprema-jela/sol?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/priprema-jela/internacionalna-kuhinja?sort[]=&per_page=100"
            )
        }

        if("Tjestenina, riža, njoki, tortilje".contentEquals(name)) {
            return listOf(
                "https://www.konzum.hr/web/t/kategorije/tjestenina-riza-njoki-tortilje/tjestenina?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/tjestenina-riza-njoki-tortilje/riza?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/tjestenina-riza-njoki-tortilje/njoki?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/tjestenina-riza-njoki-tortilje/tortilje?sort[]=&per_page=100"
            )
        }

        if("Umaci i začini".contentEquals(name)) {
            return listOf(
                "https://www.konzum.hr/web/t/kategorije/umaci-i-zacini/proizvodi-od-rajcice?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/umaci-i-zacini/kecap-senf-majoneza?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/umaci-i-zacini/umaci?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/umaci-i-zacini/zacini-i-mjesavine?sort[]=&per_page=100"
            )
        }

        if("Brzi obrok".contentEquals(name)) {
            return listOf(
                "https://www.konzum.hr/web/t/kategorije/brza-priprema?sort[]=&per_page=100"
            )
        }

        if("Konzervirano i juhe".contentEquals(name)) {
            return listOf(
                "https://www.konzum.hr/web/t/kategorije/konzervirano-juhe-gotova-jela/riblje-konzerve?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/konzervirano-juhe-gotova-jela/povrce?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/konzervirano-juhe-gotova-jela/pastete-i-mesni-proizvodi?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/konzervirano-juhe-gotova-jela/kompoti/gotova-jela?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/konzervirano-juhe-gotova-jela/juhe?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/konzervirano-juhe-gotova-jela/kompoti?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/konzervirano-juhe-gotova-jela/ajvari-i-ostalo?sort[]=&per_page=100",
            )
        }

        if("Priprema kolača".contentEquals(name)) {
            return listOf(
                "https://www.konzum.hr/web/t/kategorije/priprema-kolaca/gotove-smjese-i-mjesavine?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/priprema-kolaca/sastojci?sort[]=&per_page=100"
            )
        }

        if("Pahuljice, namazi, kave, čajevi".contentEquals(name)) {
            return listOf(
                "https://www.konzum.hr/web/t/kategorije/pahuljice-namazi-kave-cajevi/kave?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/pahuljice-namazi-kave-cajevi/namazi/marmelade-pekmezi-dzemovi?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/pahuljice-namazi-kave-cajevi/namazi/med?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/pahuljice-namazi-kave-cajevi/namazi/cokoladni?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/pahuljice-namazi-kave-cajevi/pahuljice?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/pahuljice-namazi-kave-cajevi/cajevi?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/pahuljice-namazi-kave-cajevi/cokoladni-napitci?sort[]=&per_page=100"
            )
        }

        if("Kućni ljubimci".contentEquals(name)) {
            return listOf(
                "https://www.konzum.hr/web/t/kategorije/kucni-ljubimci/psi/hrana?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/kucni-ljubimci/psi/pribor?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/kucni-ljubimci/macke/hrana?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/kucni-ljubimci/macke/pribor?sort[]=&per_page=100",
            )
        }

        if("Čišćenje i pospremanje".contentEquals(name)) {
            return listOf(
                "https://www.konzum.hr/web/t/kategorije/ciscenje-i-pospremanje/pranje-rublja/deterdzenti?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/ciscenje-i-pospremanje/pranje-rublja/omeksivaci?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/ciscenje-i-pospremanje/pranje-rublja/sredstva-za-odstranjivanje-mrlja?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/ciscenje-i-pospremanje/pranje-posuda?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/ciscenje-i-pospremanje/sredstva-za-ciscenje?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/ciscenje-i-pospremanje/vrecice-i-filteri-za-usisavace?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/ciscenje-i-pospremanje/pribor-za-ciscenje?sort[]=&per_page=100",
            )
        }

        if("Njega i higijena".contentEquals(name)) {
            return listOf(
                "https://www.konzum.hr/web/t/kategorije/njega-i-higijena/higijenski-proizvodi/toaletni-papir?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/njega-i-higijena/higijenski-proizvodi/ulosci-tamponi-intimna-njega?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/njega-i-higijena/higijenski-proizvodi/rupcici-i-vlazne-maramice?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/njega-i-higijena/higijenski-proizvodi/blazinice-stapici-za-usi-flasteri?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/njega-i-higijena/higijenski-proizvodi/prezervativi-i-lubrikanti?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/njega-i-higijena/higijenski-proizvodi/njega-ruku-i-zastita?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/njega-i-higijena/njega-tijela/sapuni?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/njega-i-higijena/njega-tijela/kupke-i-gelovi-za-tusiranje?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/njega-i-higijena/njega-tijela/dezodoransi?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/njega-i-higijena/njega-tijela/kreme-mlijeka-losioni?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/njega-i-higijena/njega-zubi/paste?sort[]=&per_page=100",
               // "https://www.konzum.hr/web/t/kategorije/njega-i-higijena/njega-zubi/cetkice?sort[]=&per_page=100", TODO not working
//                "https://www.konzum.hr/web/t/kategorije/njega-i-higijena/elektricne-cetkice?sort[]=&per_page=100", TODO not working
                "https://www.konzum.hr/web/t/kategorije/njega-i-higijena/njega-zubi/vodice-konci-ostalo?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/njega-i-higijena/njega-kose/samponi?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/njega-i-higijena/njega-kose/boje-za-kosu?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/njega-i-higijena/njega-kose/styling?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/njega-i-higijena/njega-lica/kreme?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/njega-i-higijena/njega-lica/uklanjanje-sminke?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/njega-i-higijena/njega-lica/njega-usana?sort[]=&per_page=100",
              //  "https://www.konzum.hr/web/t/kategorije/njega-i-higijena/brijanje-i-depilacija?sort[]=&per_page=100", TODO not working
                "https://www.konzum.hr/web/t/kategorije/njega-i-higijena/sredstva-za-zastitu-od-sunca?sort[]=&per_page=100",
            )
        }

        if("Kućanske potrepštine".contentEquals(name)) {
            return listOf(
                "https://www.konzum.hr/web/t/kategorije/kucanske-potrepstine/papirnati-proizvodi?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/kucanske-potrepstine/osvjezivaci-prostora?sort[]=&per_page=100",
//                "https://www.konzum.hr/web/t/kategorije/kucanske-potrepstine/mali-kucanski-aparati?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/kucanske-potrepstine/vrecice?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/kucanske-potrepstine/baterije-i-zarulje?sort[]=&per_page=100",
                "https://www.konzum.hr/web/t/kategorije/kucanske-potrepstine/ostale-potrepstine?sort[]=&per_page=100"
            )
        }

        /*
        TODO
        Pazim što jedem
        Posuđe
        Party
        Škola i ured
        Auto program
        */

        // TODO KOLAĆI I TORTE https://www.konzum.hr/web/t/kategorije/pekarnica/kolaci-i-torte
        // TODO smrtnuta hrana https://www.konzum.hr/web/t/kategorije/voce-i-povrce/smrznuto-voce-i-povrce

        return emptyList();
     }

    private fun parseFromUrl(url:String, category: Category): List<Item> {
        println(url)
        val doc = Jsoup.connect(url).get();
        val productListWrapper = doc.select(".product-list");

        return productListWrapper.select(".product-item")
            .stream()
            .map{ toItem(it, category) }
            .collect(Collectors.toList())
    }

    private fun toItem(element: Element, category: Category): Item {
        val productList: Element = element.select("div[data-ga-name]").first()!!
        val name = productList.attr("data-ga-name")
        val price = productList.attr("data-ga-price")
            .replace('.', ' ')
            .trim()
            .replace(',', '.')
            .replace(" kn", "")
            .trim()
        val imgId = element.select(".link-to-product")
                .select("img")
                .attr("src")

        val itemPrice = Price(
            originalPrice = null, // TODO original price because of daily deal
            actionPrice = BigDecimal(price),
            activeFrom = LocalDate.now().minusDays(-1),
            activeTo = LocalDate.now().plusDays(5), // TODO should change this extract store info
            previous = null,
        )
        return Item(
            name = name,
            longName = name,
            description = name,
            imageId = imgId,
            category = category,
            store = konzumStore,
            price = itemPrice,
            userName = "Hellena",
        )
    }
}
