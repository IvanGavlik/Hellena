package com.hellena.predict.item.service

import com.hellena.predict.api.model.ItemInsertDto;
import com.hellena.predict.item.Item
import com.hellena.predict.item.ItemRepository
import com.hellena.predict.item.category.Category
import com.hellena.predict.item.category.CategoryRepository
import com.hellena.predict.item.price.Price
import com.hellena.predict.item.store.Store
import com.hellena.predict.item.store.StoreRepository
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.util.*
import javax.transaction.Transactional

interface ItemInsertService {
    fun createItem(itemInsertDto: ItemInsertDto)
}

@Service
class ItemInsertServiceImpl(val categoryRepository: CategoryRepository,
                            val storeRepository: StoreRepository,
                            val itemRepository: ItemRepository): ItemInsertService {
    @Transactional
    override fun createItem(itemInsertDto: ItemInsertDto) {

        var categoryOptional: Optional<Category> = categoryRepository.findById(itemInsertDto.category);
        var storeOptional: Optional<Store> = storeRepository.findById(itemInsertDto.store);

        var price = Price(
            originalPrice = itemInsertDto.orginalPrice,
            actionPrice = itemInsertDto.actionPrice,
            activeFrom = itemInsertDto.activeFrom,
            activeTo = itemInsertDto.activeTo,
        );

        var item = Item(
            name = itemInsertDto.name,
            imageId = itemInsertDto.image,
            category =  categoryOptional.orElseThrow { RuntimeException("Category not found")  },
            store = storeOptional.orElseThrow { RuntimeException("Store not found")  },
            price = price,
            userName = itemInsertDto.user
        );
        itemRepository.save(item);

    }
}
