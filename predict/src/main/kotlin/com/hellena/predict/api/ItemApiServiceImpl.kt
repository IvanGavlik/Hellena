package com.hellena.predict.api

import com.hellena.predict.api.model.CategoryDto
import com.hellena.predict.api.model.ItemDto
import com.hellena.predict.api.model.SearchItemDto
import com.hellena.predict.item.ItemSearch
import com.hellena.predict.item.service.ItemService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.CrossOrigin

@Service
class ItemApiServiceImpl(val itemService: ItemService): ItemApiDelegate {


    override fun getItems(): ResponseEntity<List<ItemDto>> {
        val headers = HttpHeaders()
        headers.accessControlAllowOrigin = "*"
        return ResponseEntity(itemService.getItems(), headers, HttpStatus.OK)
    }

    override fun getCategories(): ResponseEntity<List<CategoryDto>> {
        val headers = HttpHeaders()
        headers.accessControlAllowOrigin = "*"
        headers.accessControlAllowMethods = listOf(HttpMethod.POST, HttpMethod.GET, HttpMethod.OPTIONS)
        headers.accessControlAllowCredentials = true
        headers.accessControlAllowHeaders = listOf("Content-Type", "Depth, User-Agent", "X-File-Size", "X-Requested-With", "If-Modified-Since",
            "X-File-Name", "Cache-Control");
        return ResponseEntity(itemService.getCategories(), headers, HttpStatus.OK)
    }

    // https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
    @CrossOrigin(origins = ["http://localhost:8080/v1/item/search"])
    override fun searchItems(searchItemDto: SearchItemDto?): ResponseEntity<List<ItemDto>> {

        val search = ItemSearch(
            name = searchItemDto?.name,
            categoryId = searchItemDto?.categoryId?.longValueExact(),
            cityId = searchItemDto?.cityId?.longValueExact(),
            priceMax = searchItemDto?.priceMax,
            priceMIn = searchItemDto?.priceMIn,
            page = null
        )
        return ResponseEntity(itemService.getItems(search), HttpStatus.OK);
    }

}
