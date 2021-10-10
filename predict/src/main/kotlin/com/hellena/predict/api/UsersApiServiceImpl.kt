package com.hellena.predict.api

import org.springframework.stereotype.Service

@Service
class UsersApiServiceImpl: UsersApiService {
    override fun getUsers(): List<String> {
        return listOf("Marko");
    }
}
