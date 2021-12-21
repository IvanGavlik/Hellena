package com.hellena.predict.api

import com.hellena.predict.api.model.CategoryDto
import com.hellena.predict.api.model.ItemDto
import com.hellena.predict.item.service.ItemService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class ItemApiServiceImpl(val itemService: ItemService): ItemApiDelegate {


    override fun getItems(): ResponseEntity<List<ItemDto>> {
        val headers = HttpHeaders()
        headers.accessControlAllowOrigin = "*"
        return ResponseEntity(itemService.getItems(), headers, HttpStatus.OK)
    }

     fun getCategories(): ResponseEntity<List<CategoryDto>> {
        val headers = HttpHeaders()
        headers.accessControlAllowOrigin = "*"
        return ResponseEntity(itemService.getCategories(), headers, HttpStatus.OK)
    }

    override fun getCategories(test: kotlin.Any): ResponseEntity<List<CategoryDto>> {
        println("decode " + String(Base64.getDecoder().decode(test.toString())));
        val headers = HttpHeaders()
        headers.accessControlAllowOrigin = "*"
        return ResponseEntity(itemService.getCategories(), headers, HttpStatus.OK)
    }

}
