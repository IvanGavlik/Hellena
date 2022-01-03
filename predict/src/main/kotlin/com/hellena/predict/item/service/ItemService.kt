package com.hellena.predict.item.service

import com.hellena.predict.api.model.CategoryDto
import com.hellena.predict.api.model.ItemDto
import com.hellena.predict.item.Item
import com.hellena.predict.item.ItemRepository
import com.hellena.predict.item.ItemSearch
import com.hellena.predict.item.category.Category
import com.hellena.predict.item.category.CategoryRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors

interface ItemService {
    fun getItems(): List<ItemDto>;
    fun getItems(search: ItemSearch): List<ItemDto>;
    fun getCategories(): List<CategoryDto>;
}

@Service
class ItemServiceImpl(val itemRepository: ItemRepository,
                      val categoryRepository: CategoryRepository
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

    override fun getItems(search: ItemSearch): List<ItemDto> {
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
}
