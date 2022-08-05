package com.hellena.predict.item.service

import com.hellena.predict.api.model.*
import com.hellena.predict.fetch.interspar.Interspar
import com.hellena.predict.fetch.kaufland.Kaufland
import com.hellena.predict.fetch.konzum.Konzum
import com.hellena.predict.fetch.lidl.Lidl
import com.hellena.predict.fetch.plodine.Plodine
import com.hellena.predict.item.Item
import com.hellena.predict.item.ItemRepository
import com.hellena.predict.item.ItemSearch
import com.hellena.predict.item.category.Category
import com.hellena.predict.item.category.CategoryRepository
import com.hellena.predict.item.feature.FeatureFactory
import com.hellena.predict.item.feature.ItemFeatureType
import com.hellena.predict.item.location.Location
import com.hellena.predict.item.location.LocationRepository
import com.hellena.predict.item.store.Store
import com.hellena.predict.item.store.StoreRepository
import com.hellena.predict.search.Page
import com.hellena.predict.search.Sort
import org.springframework.stereotype.Service
import java.util.stream.Collectors
import javax.transaction.Transactional

interface ItemService {
    fun getItems(): List<ItemDto>;
    fun getItemsNames(name: String): List<String>;
    fun getItems(search: ItemSearch, feature: ItemFeatureType?): PageItemDto;
    fun getCategories(): List<CategoryDto>;
    fun getCities(): List<CityDto>;
    fun getStores(): List<StoreDto>;
}

@Service
class ItemServiceImpl(val itemRepository: ItemRepository,
                      val categoryRepository: CategoryRepository,
                      val locationRepository: LocationRepository,
                      val storeRepository: StoreRepository,
                      val featureFactory: FeatureFactory,
                      ): ItemService {

    override fun getItems(): List<ItemDto> {
        /*

        try {
            Plodine(this.storeRepository, this.categoryRepository).fetch().forEach { it -> itemRepository.save(it) }
        } catch (ex: java.lang.Exception) {
            println("Plodine " + ex);
        }


        try {
            Konzum(categoryRepository.findAll(), this.storeRepository).fetch().forEach { it ->         itemRepository.save(it) }
        } catch (ex: java.lang.Exception) {
            println("Konzum " + ex);
        }

        try {
            Kaufland(this.categoryRepository.findAll(), this.storeRepository).fetch().forEach { this.itemRepository.save(it) }         // TODO PAZI DATUM
        } catch (ex: java.lang.Exception) {
            println("Kaufland " + ex);
        }

        // TODO must manually update category
        // TODO must manually update dates
         try {
            Interspar(this.storeRepository, this.categoryRepository.findById(1L).get()).fetch().forEach { this.itemRepository.save(it) }
        } catch (ex: java.lang.Exception) {
                println("Interspar " + ex);
        }

        */

       // TODO must manually update category
       // Lidl(this.storeRepository, this.categoryRepository.findById(1L).get()).fetch().forEach {  this.itemRepository.save(it) };

      //  Lidl(this.storeRepository, this.categoryRepository.findById(1L).get()).fetch().forEach {  this.itemRepository.save(it) };

        println("Done")


        return itemRepository.findAll().stream()
            .map {  toItemDTO(it) }
            .collect(Collectors.toList())
    }

    override fun getItemsNames(name: String): List<String> {
        class DefPage: Page {
            override fun getIndex(): Long {
                return 1;
            }

            override fun getSize(): Long {
                return 12;
            }

            override fun getSort(): List<Sort>? {
                return null;
            }
        }
        val search = ItemSearch(
            name = name,
            nameStarsWith = true,
            categoryIds = emptyList(),
            storeIds = emptyList(),
            priceMIn = null,
            priceMax = null,
            page = DefPage()
        );

        val names: MutableList<String> = mutableListOf();
        this.getItems(search, null)
            .page!!.stream()
            .filter { item -> item.name != null }
            .forEach { item ->  names.add(item.name!!)  }

        return names.toList();
    }

    override fun getCategories(): List<CategoryDto> {
        return categoryRepository.findAll().stream()
            .map { toCategoryDto(it) }
            .collect(Collectors.toList());
    }

    override fun getCities(): List<CityDto> {
        return locationRepository.findAll().stream()
            .map { toCityDto(it) }
            .collect(Collectors.toList());
    }

    override fun getStores(): List<StoreDto> {
        return storeRepository.findAll().stream()
            .map { toStoreDto(it) }
            .collect(Collectors.toList());
    }

    /**
     * How data is fetched
     * Fetch from DB is there is no results for Konzum
     * then fetch from Kozum web service (only if name is set)
     */
    @Transactional()
    override fun getItems(search: ItemSearch, feature: ItemFeatureType?): PageItemDto {
        // TODO implement feature
        val result = this.itemRepository.search(search);
        val list = result.elements.stream()
            .map {  toItemDTO(it) }
            .collect(Collectors.toList());
        return PageItemDto(result.size, list);
    }

    private fun toItemDTO(it: Item): ItemDto {
        var dto = ItemDto(
            id = it.id,
            name = it.name,
            storeName = it.store.name,
            orginalPrice = it.price.originalPrice,
            actionPrice = it.price.actionPrice,
            activeFrom = it.price.activeFrom,
            activeTo = it.price.activeTo,
            discountPrice = it.discountPrice,
            discountPercentage = it.discountPercentage,
            imageName = it.imageId,
        );
        return dto;
    }

    private fun toCategoryDto(it: Category): CategoryDto {
        val dto = CategoryDto(
            id = it.id,
            name = it.name,
            description = it.description
        )
        return dto;
    }

    private fun toCityDto(it: Location): CityDto {
        val dto = CityDto(
            id = it.id,
            name = it.city
        );
        return dto;
    }

    private fun toStoreDto(it: Store): StoreDto {
        val dto = StoreDto(
            id = it.id,
            name = it.name,
        )
        return dto;
    }

}
