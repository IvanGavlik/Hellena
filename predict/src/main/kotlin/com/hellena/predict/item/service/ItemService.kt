package com.hellena.predict.item.service

import com.hellena.predict.api.model.CategoryDto
import com.hellena.predict.api.model.CityDto
import com.hellena.predict.api.model.ItemDto
import com.hellena.predict.api.model.StoreDto
import com.hellena.predict.item.Item
import com.hellena.predict.item.ItemRepository
import com.hellena.predict.item.ItemSearch
import com.hellena.predict.item.category.Category
import com.hellena.predict.item.category.CategoryRepository
import com.hellena.predict.item.feature.FeatureFactory
import com.hellena.predict.item.location.Location
import com.hellena.predict.item.location.LocationRepository
import com.hellena.predict.item.store.Store
import com.hellena.predict.item.store.StoreRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors

interface ItemService {
    fun getItems(): List<ItemDto>;
    fun getItems(search: ItemSearch): List<ItemDto>;
    fun getCategories(): List<CategoryDto>;
    fun getCities(): List<CityDto>;
    fun getStores(): List<StoreDto>;
}

@Service
class ItemServiceImpl(val itemRepository: ItemRepository,
                      val categoryRepository: CategoryRepository,
                      val locationRepository: LocationRepository,
                      val storeRepository: StoreRepository,
                      val featureFactory: FeatureFactory
                      ): ItemService {

    override fun getItems(): List<ItemDto> {
        return itemRepository.findAll().stream()
            .map { toItemDTO(it) }
            .collect(Collectors.toList())
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

    override fun getItems(search: ItemSearch): List<ItemDto> {
        this.featureFactory.getFeature("test").fetch();
        return itemRepository.search(search).stream()
            .map { toItemDTO(it) }
            .collect(Collectors.toList())
    }

    private fun toItemDTO(it: Item): ItemDto {
        val dto = ItemDto(
            id = it.id.toString(),
            name = it.name,
            storeName = it.store.name,
            orginalPrice = it.price.originalPrice,
            actionPrice = it.price.actionPrice,
        );
        return dto;
    }

    private fun toCategoryDto(it: Category): CategoryDto {
        val dto = CategoryDto(
            id = it.id.toString(),
            name = it.name,
            description = it.description
        )
        return dto;
    }

    private fun toCityDto(it: Location): CityDto {
        val dto = CityDto(
            id = it.id.toString(),
            name = it.city
        );
        return dto;
    }

    private fun toStoreDto(it: Store): StoreDto {
        val dto = StoreDto(
            id = it.id.toString(),
            name = it.name,
        )
        return dto;
    }

}
