package com.hellena.predict.item.service

import com.hellena.predict.api.model.ItemDto
import com.hellena.predict.item.Item
import com.hellena.predict.item.ItemRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors

interface ItemService {
    fun getItems(): List<ItemDto>;
}

@Service
class ItemServiceImpl(val itemRepository: ItemRepository): ItemService {
    override fun getItems(): List<ItemDto> {
        return itemRepository.findAll().stream()
            .map { toItemDTO(it) }
            .collect(Collectors.toList())
    }

    private fun toItemDTO(it: Item): ItemDto {
        val dto = ItemDto(
            name = it.name,
            storeName = it.store.name,
            orginalPrice = it.price.originalPrice,
            actionPrice = it.price.actionPrice,
        );
        return dto;
    }
}
