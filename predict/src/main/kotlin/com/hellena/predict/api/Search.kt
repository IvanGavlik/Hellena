package com.hellena.predict.api

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Search {


    @GetMapping(
        value = ["/search"],
    )
    fun search(): ResponseEntity<Void> {
        val headers = HttpHeaders()
        headers.accessControlAllowOrigin = "*"
        return ResponseEntity(headers, HttpStatus.OK);
    }

}
