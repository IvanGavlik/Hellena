package com.hellena.predict.item.category

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository



@Table("category")
data class Category (
    @Id val id: Long,
    @Column val name: String,
    @Column val description: String,
    @Column("sub_category") val subCategory: Long?, // TODO one to one
    ) {
}

interface CategoryRepository: CrudRepository<Category, Long> {
    override fun findAll(): List<Category>
}
