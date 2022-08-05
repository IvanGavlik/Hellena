package com.hellena.predict.api

import com.hellena.predict.api.model.*
import com.hellena.predict.item.ItemSearch
import com.hellena.predict.item.service.ItemInsertService
import com.hellena.predict.item.service.ItemService
import com.hellena.predict.search.Page
import com.hellena.predict.search.Sort
import com.hellena.predict.search.SortDirection
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.math.BigDecimal


@Service
class ItemApiServiceImpl(val itemService: ItemService, val itemInsertService: ItemInsertService): ItemApiDelegate {


    override fun createItem(itemInsertDto: ItemInsertDto): ResponseEntity<Unit> {
        itemInsertService.createItem(itemInsertDto);
        return ResponseEntity(HttpStatus.OK);
    }

    override fun getItems(): ResponseEntity<List<ItemDto>> {
        val headers = HttpHeaders()
        headers.accessControlAllowOrigin = "*"
        return ResponseEntity(itemService.getItems(), headers, HttpStatus.OK)
    }

    override fun searchItemNames(body: String): ResponseEntity<List<String>> {
        val headers = HttpHeaders()
        headers.accessControlAllowOrigin = "*"
        return ResponseEntity(itemService.getItemsNames(body), HttpStatus.OK);
    }

    override fun getCategories(): ResponseEntity<List<CategoryDto>> {
        return ResponseEntity(itemService.getCategories(), HttpStatus.OK)
    }

    override fun getCities(): ResponseEntity<List<CityDto>> {
        return ResponseEntity(itemService.getCities(), HttpStatus.OK)
    }

    override fun getStores(): ResponseEntity<List<StoreDto>> {
        return ResponseEntity(itemService.getStores(), HttpStatus.OK)
    }

    override fun searchItems(pageIndex: kotlin.Long,
                    pageSize: kotlin.Long,
                    categoryIds: kotlin.collections.List<kotlin.Long>?,
                    storeIds: kotlin.collections.List<kotlin.Long>?,
                    pageSortName: kotlin.String?,
                    pageSortDirection: kotlin.String?,
                    name: kotlin.String?,
                    priceMin: java.math.BigDecimal?,
                    priceMax: java.math.BigDecimal?): ResponseEntity<PageItemDto> {
        val search = ItemSearch(
            name = name,
            nameStarsWith = false,
            storeIds = storeIds ?: emptyList<Long>(),
            categoryIds = categoryIds ?: emptyList<Long>(),
            priceMax = priceMax,
            priceMIn = priceMin,
            page = toPage(pageIndex, pageSize, pageSortName, pageSortDirection)
        )
        // TODO implement feature
        return ResponseEntity(itemService.getItems(search, null), HttpStatus.OK);

    }

    fun toPage(pageIndex: Long,
                pageSize: Long,
                pageSortName: String?,
                pageSortDirection: String?): Page {
        return object: Page {
            override fun getIndex(): Long {
                return pageIndex;
            }
            override fun getSize(): Long {
                return pageSize
            }
            override fun getSort(): List<Sort> {
                return listOf(
                    object : Sort {
                        override fun getName(): String {
                            return pageSortName!!;
                        }

                        override fun getDir(): SortDirection {
                            return SortDirection.ASC;
                        }
                    }
                )
            }
        }
    }

    fun toSort(sortDto: SortDto): Sort {
        return object: Sort {
            override fun getName(): String {
                return sortDto.name
            }
            override fun getDir(): SortDirection {
                return SortDirection.valueOf(sortDto.direction.name);
            }
        }
    }

}
