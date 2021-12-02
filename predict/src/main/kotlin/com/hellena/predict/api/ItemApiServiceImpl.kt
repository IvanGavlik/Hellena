package com.hellena.predict.api

import com.hellena.predict.api.model.ItemDto
import com.hellena.predict.item.service.ItemService
import org.springframework.stereotype.Service

@Service
class ItemApiServiceImpl(val itemService: ItemService): ItemApiService {
    override fun getItems(): List<ItemDto> {
        return itemService.getItems();
    }
}
